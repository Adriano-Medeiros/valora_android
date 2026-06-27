package br.com.agrobox.ruralcoleta.domain.exportacao

import android.content.Context
import br.com.agrobox.ruralcoleta.data.export.ExcelExportService
import br.com.agrobox.ruralcoleta.data.export.PdfExportService
import br.com.agrobox.ruralcoleta.data.export.ZipExportService
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ExportacaoUseCase(
    private val coletaRepository: ColetaRepository,
    private val modeloColetaRepository: ModeloColetaRepository,
    private val respostaColetaRepository: RespostaColetaRepository,
    private val fotoColetaRepository: FotoColetaRepository,
    private val benfeitoriaRepository: BenfeitoriaRepository,
    private val fotoBenfeitoriaRepository: FotoBenfeitoriaRepository,
    private val excelExportService: ExcelExportService = ExcelExportService(),
    private val zipExportService: ZipExportService = ZipExportService(),
    private val pdfExportService: PdfExportService = PdfExportService()
) {

    suspend fun exportar(
        context: Context,
        tipo: TipoExportacao,
        coletasIds: List<Long>
    ): ExportacaoResultado = withContext(Dispatchers.IO) {
        if (coletasIds.isEmpty()) {
            throw IllegalArgumentException("Selecione pelo menos uma coleta para exportar.")
        }

        val dados = carregarDadosExportacao(
            coletasIds = coletasIds
        )

        when (tipo) {
            TipoExportacao.EXCEL -> {
                val arquivo = excelExportService.exportarColetas(
                    context = context,
                    nomeModelo = dados.nomeModelo,
                    coletas = dados.coletas,
                    variaveis = dados.variaveis,
                    respostasPorColeta = dados.respostasPorColeta,
                    fotoPrincipalPorColeta = dados.fotoPrincipalPorColeta,
                    fotosBenfeitoriasPorColeta = dados.fotosBenfeitoriasPorColeta
                )

                ExportacaoResultado(
                    arquivo = arquivo,
                    tipo = tipo,
                    mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    tituloCompartilhamento = "Compartilhar planilha"
                )
            }

            TipoExportacao.ZIP -> {
                val arquivo = zipExportService.exportarZipCompleto(
                    context = context,
                    dados = dados,
                    excelExportService = excelExportService,
                    pdfExportService = pdfExportService
                )

                ExportacaoResultado(
                    arquivo = arquivo,
                    tipo = tipo,
                    mimeType = "application/zip",
                    tituloCompartilhamento = "Compartilhar exportação completa"
                )
            }

            TipoExportacao.PDF -> {
                val arquivo = pdfExportService.exportarRelatorioPdf(
                    context = context,
                    dados = dados
                )

                ExportacaoResultado(
                    arquivo = arquivo,
                    tipo = tipo,
                    mimeType = "application/pdf",
                    tituloCompartilhamento = "Compartilhar relatório PDF"
                )
            }
        }
    }

    private suspend fun carregarDadosExportacao(
        coletasIds: List<Long>
    ): DadosExportacao {
        val coletas = coletaRepository.listarPorIds(coletasIds)

        if (coletas.isEmpty()) {
            throw IllegalArgumentException("Nenhuma coleta encontrada para exportação.")
        }

        val modeloId = coletas
            .firstOrNull()
            ?.modeloColetaId

        val modelo = modeloId?.let { id ->
            modeloColetaRepository.buscarModeloPorId(id)
        }

        val variaveis = modeloId?.let { id ->
            modeloColetaRepository
                .listarVariaveisDetalhadasPorModelo(id)
                .first()
        }.orEmpty()

        val respostasPorColeta = coletas.associate { coleta ->
            coleta.id to respostaColetaRepository
                .listarPorColeta(coleta.id)
                .first()
        }

        val fotosGeraisPorColeta = coletas.associate { coleta ->
            coleta.id to fotoColetaRepository
                .listarPorColeta(coleta.id)
                .first()
        }

        val fotoPrincipalPorColeta = fotosGeraisPorColeta.mapValues { item ->
            item.value.firstOrNull()?.caminhoArquivo
        }

        val benfeitoriasPorColeta = coletas.associate { coleta ->
            coleta.id to benfeitoriaRepository
                .listarPorColeta(coleta.id)
                .first()
        }

        val todasBenfeitorias = benfeitoriasPorColeta.values.flatten()

        val fotosPorBenfeitoria = todasBenfeitorias.associate { benfeitoria ->
            benfeitoria.id to fotoBenfeitoriaRepository
                .listarPorBenfeitoria(benfeitoria.id)
                .first()
        }

        val fotosBenfeitoriasPorColeta = coletas.associate { coleta ->
            val benfeitorias = benfeitoriasPorColeta[coleta.id].orEmpty()

            val fotos = benfeitorias.flatMap { benfeitoria ->
                fotosPorBenfeitoria[benfeitoria.id]
                    .orEmpty()
                    .map { foto -> foto.caminhoArquivo }
            }

            coleta.id to fotos
        }

        val nomeModelo = modelo?.nome ?: if (coletas.size == 1) {
            coletas.first().nomeReferencia
        } else {
            "Coletas"
        }

        return DadosExportacao(
            modelo = modelo,
            nomeModelo = nomeModelo,
            coletas = coletas,
            variaveis = variaveis,
            respostasPorColeta = respostasPorColeta,
            fotoPrincipalPorColeta = fotoPrincipalPorColeta,
            fotosBenfeitoriasPorColeta = fotosBenfeitoriasPorColeta,
            fotosGeraisPorColeta = fotosGeraisPorColeta,
            benfeitoriasPorColeta = benfeitoriasPorColeta,
            fotosPorBenfeitoria = fotosPorBenfeitoria
        )
    }
}
