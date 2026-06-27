package br.com.agrobox.ruralcoleta.data.backup

import android.content.Context
import android.net.Uri
import androidx.sqlite.db.SimpleSQLiteQuery
import br.com.agrobox.ruralcoleta.BuildConfig
import br.com.agrobox.ruralcoleta.data.local.database.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class BackupService {

    suspend fun gerarBackupCompleto(
        context: Context
    ): File = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val dataHora = SimpleDateFormat(
            "yyyy-MM-dd_HHmmss",
            Locale("pt", "BR")
        ).format(Date())

        val nomeBase = "RuralColeta_Backup_$dataHora"
        val pastaTrabalho = File(appContext.cacheDir, "backup_tmp")
        val pastaBackup = File(pastaTrabalho, nomeBase)
        val arquivoZip = File(appContext.cacheDir, "$nomeBase.zip")

        if (pastaTrabalho.exists()) {
            pastaTrabalho.deleteRecursively()
        }

        if (arquivoZip.exists()) {
            arquivoZip.delete()
        }

        pastaBackup.mkdirs()

        gravarMetadata(
            destino = File(pastaBackup, "metadata.json"),
            nomeBackup = nomeBase
        )

        copiarBancosDeDados(
            context = appContext,
            destino = File(pastaBackup, "databases")
        )

        copiarDiretorioSeExistir(
            origem = appContext.filesDir,
            destino = File(pastaBackup, "files"),
            nomesIgnorados = setOf(
                "exportacoes",
                "backup_tmp",
                "restore_tmp"
            )
        )

        copiarDiretorioSeExistir(
            origem = appContext.getExternalFilesDir(null),
            destino = File(pastaBackup, "external_files"),
            nomesIgnorados = setOf(
                "exportacoes",
                "backup_tmp",
                "restore_tmp"
            )
        )

        copiarDiretorioSeExistir(
            origem = File(appContext.applicationInfo.dataDir, "shared_prefs"),
            destino = File(pastaBackup, "shared_prefs"),
            nomesIgnorados = emptySet()
        )

        compactarPasta(
            pastaOrigem = pastaBackup,
            arquivoZip = arquivoZip
        )

        pastaTrabalho.deleteRecursively()

        arquivoZip
    }

    suspend fun restaurarBackupCompleto(
        context: Context,
        backupUri: Uri
    ): Unit = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val pastaRestore = File(appContext.cacheDir, "restore_tmp")
        val arquivoTemporario = File(appContext.cacheDir, "backup_para_restaurar.zip")

        if (pastaRestore.exists()) {
            pastaRestore.deleteRecursively()
        }

        if (arquivoTemporario.exists()) {
            arquivoTemporario.delete()
        }

        pastaRestore.mkdirs()

        appContext.contentResolver.openInputStream(backupUri)?.use { input ->
            FileOutputStream(arquivoTemporario).use { output ->
                input.copyTo(output)
            }
        } ?: throw IllegalArgumentException("Não foi possível abrir o arquivo de backup.")

        extrairZipComSeguranca(
            arquivoZip = arquivoTemporario,
            destino = pastaRestore
        )

        validarBackup(pastaRestore)

        DatabaseProvider.closeDatabase()

        restaurarBancosDeDados(
            context = appContext,
            origem = File(pastaRestore, "databases")
        )

        restaurarDiretorio(
            origem = File(pastaRestore, "files"),
            destino = appContext.filesDir
        )

        restaurarDiretorio(
            origem = File(pastaRestore, "external_files"),
            destino = appContext.getExternalFilesDir(null)
        )

        restaurarDiretorio(
            origem = File(pastaRestore, "shared_prefs"),
            destino = File(appContext.applicationInfo.dataDir, "shared_prefs")
        )

        DatabaseProvider.closeDatabase()

        arquivoTemporario.delete()
        pastaRestore.deleteRecursively()
    }

    private fun gravarMetadata(
        destino: File,
        nomeBackup: String
    ) {
        destino.parentFile?.mkdirs()

        val metadata = JSONObject().apply {
            put("tipo", "RURALCOLETA_BACKUP")
            put("versaoBackup", 1)
            put("nomeBackup", nomeBackup)
            put("applicationId", BuildConfig.APPLICATION_ID)
            put("versionCode", BuildConfig.VERSION_CODE)
            put("versionName", BuildConfig.VERSION_NAME)
            put("databaseName", DatabaseProvider.DATABASE_NAME)
            put("geradoEm", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("pt", "BR")).format(Date()))
        }

        destino.writeText(
            text = metadata.toString(4),
            charset = Charsets.UTF_8
        )
    }

    private fun copiarBancosDeDados(
        context: Context,
        destino: File
    ) {
        destino.mkdirs()

        prepararBancoParaCopia(context)

        val nomesBancos = context.databaseList()
            .toList()
            .ifEmpty {
                listOf(DatabaseProvider.DATABASE_NAME)
            }
            .distinct()

        nomesBancos.forEach { nomeBanco ->
            val arquivoBanco = context.getDatabasePath(nomeBanco)

            val arquivosRelacionados = listOf(
                arquivoBanco,
                File(arquivoBanco.path + "-wal"),
                File(arquivoBanco.path + "-shm"),
                File(arquivoBanco.path + "-journal")
            )

            arquivosRelacionados.forEach { arquivo ->
                if (arquivo.exists() && arquivo.isFile) {
                    arquivo.copyTo(
                        target = File(destino, arquivo.name),
                        overwrite = true
                    )
                }
            }
        }
    }

    private fun prepararBancoParaCopia(
        context: Context
    ) {
        try {
            val database = DatabaseProvider.getDatabase(context)
            database.query(
                SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)")
            ).use {
                // Apenas força o checkpoint antes da cópia.
            }
        } catch (_: Exception) {
            // O backup ainda continua copiando os arquivos existentes.
        }
    }

    private fun restaurarBancosDeDados(
        context: Context,
        origem: File
    ) {
        if (!origem.exists()) {
            throw IllegalArgumentException("O backup não contém a pasta de banco de dados.")
        }

        val pastaBancos = context.getDatabasePath(DatabaseProvider.DATABASE_NAME).parentFile
            ?: throw IllegalStateException("Não foi possível localizar a pasta de bancos de dados.")

        pastaBancos.mkdirs()

        limparBancosAtuais(context)

        origem.listFiles()
            .orEmpty()
            .filter { it.isFile }
            .forEach { arquivoBackup ->
                arquivoBackup.copyTo(
                    target = File(pastaBancos, arquivoBackup.name),
                    overwrite = true
                )
            }
    }

    private fun limparBancosAtuais(
        context: Context
    ) {
        val nomesBancos = context.databaseList()
            .toList()
            .ifEmpty {
                listOf(DatabaseProvider.DATABASE_NAME)
            }
            .distinct()

        nomesBancos.forEach { nomeBanco ->
            val arquivoBanco = context.getDatabasePath(nomeBanco)

            listOf(
                arquivoBanco,
                File(arquivoBanco.path + "-wal"),
                File(arquivoBanco.path + "-shm"),
                File(arquivoBanco.path + "-journal")
            ).forEach { arquivo ->
                if (arquivo.exists()) {
                    arquivo.delete()
                }
            }
        }
    }

    private fun copiarDiretorioSeExistir(
        origem: File?,
        destino: File,
        nomesIgnorados: Set<String>
    ) {
        if (origem == null || !origem.exists()) {
            return
        }

        copiarDiretorio(
            origem = origem,
            destino = destino,
            nomesIgnorados = nomesIgnorados
        )
    }

    private fun copiarDiretorio(
        origem: File,
        destino: File,
        nomesIgnorados: Set<String>
    ) {
        if (!origem.exists()) {
            return
        }

        if (origem.isDirectory) {
            if (nomesIgnorados.contains(origem.name)) {
                return
            }

            destino.mkdirs()

            origem.listFiles().orEmpty().forEach { filho ->
                copiarDiretorio(
                    origem = filho,
                    destino = File(destino, filho.name),
                    nomesIgnorados = nomesIgnorados
                )
            }
        } else {
            destino.parentFile?.mkdirs()
            origem.copyTo(
                target = destino,
                overwrite = true
            )
        }
    }

    private fun restaurarDiretorio(
        origem: File,
        destino: File?
    ) {
        if (!origem.exists() || destino == null) {
            return
        }

        if (destino.exists()) {
            destino.deleteRecursively()
        }

        destino.mkdirs()

        origem.listFiles().orEmpty().forEach { filho ->
            copiarDiretorio(
                origem = filho,
                destino = File(destino, filho.name),
                nomesIgnorados = emptySet()
            )
        }
    }

    private fun compactarPasta(
        pastaOrigem: File,
        arquivoZip: File
    ) {
        ZipOutputStream(
            FileOutputStream(arquivoZip)
        ).use { zipOut ->
            pastaOrigem.walkTopDown()
                .filter { arquivo -> arquivo.isFile }
                .forEach { arquivo ->
                    val caminhoRelativo = arquivo
                        .relativeTo(pastaOrigem)
                        .path
                        .replace("\\", "/")

                    zipOut.putNextEntry(
                        ZipEntry(caminhoRelativo)
                    )

                    arquivo.inputStream().use { input ->
                        input.copyTo(zipOut)
                    }

                    zipOut.closeEntry()
                }
        }
    }

    private fun extrairZipComSeguranca(
        arquivoZip: File,
        destino: File
    ) {
        val destinoCanonico = destino.canonicalFile

        ZipInputStream(
            arquivoZip.inputStream()
        ).use { zipInput ->
            var entrada = zipInput.nextEntry

            while (entrada != null) {
                val arquivoDestino = File(destinoCanonico, entrada.name)
                val arquivoDestinoCanonico = arquivoDestino.canonicalFile

                if (!arquivoDestinoCanonico.path.startsWith(destinoCanonico.path)) {
                    throw IOException("Arquivo inválido dentro do backup.")
                }

                if (entrada.isDirectory) {
                    arquivoDestinoCanonico.mkdirs()
                } else {
                    arquivoDestinoCanonico.parentFile?.mkdirs()

                    FileOutputStream(arquivoDestinoCanonico).use { output ->
                        zipInput.copyTo(output)
                    }
                }

                zipInput.closeEntry()
                entrada = zipInput.nextEntry
            }
        }
    }

    private fun validarBackup(
        pastaRestore: File
    ) {
        val metadataFile = File(pastaRestore, "metadata.json")

        if (!metadataFile.exists()) {
            throw IllegalArgumentException("Backup inválido: metadata.json não encontrado.")
        }

        val metadata = JSONObject(
            metadataFile.readText(Charsets.UTF_8)
        )

        val tipo = metadata.optString("tipo")

        if (tipo != "RURALCOLETA_BACKUP") {
            throw IllegalArgumentException("Este arquivo não é um backup válido do RuralColeta.")
        }

        val pastaDatabases = File(pastaRestore, "databases")

        if (!pastaDatabases.exists()) {
            throw IllegalArgumentException("Backup inválido: banco de dados não encontrado.")
        }
    }
}
