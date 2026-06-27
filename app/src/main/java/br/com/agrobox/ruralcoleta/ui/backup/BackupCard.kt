package br.com.agrobox.ruralcoleta.ui.backup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BackupCard(
    modifier: Modifier = Modifier,
    viewModel: BackupViewModel = viewModel(
        factory = BackupViewModelFactory()
    )
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var uriSelecionadoParaRestaurar by remember {
        mutableStateOf<Uri?>(null)
    }

    val seletorBackup = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            uriSelecionadoParaRestaurar = uri
        }
    }

    val verde = Color(0xFF00823B)
    val vermelho = Color(0xFFD32F2F)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Backup e restauração",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Gere uma cópia completa dos dados locais do aplicativo, incluindo banco de dados, fotos e preferências.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            if (uiState.processando) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = verde
                )
            }

            uiState.mensagemSucesso?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = verde,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            uiState.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = vermelho,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    viewModel.gerarBackup(context)
                },
                enabled = !uiState.processando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Text(
                    text = if (uiState.processando) {
                        "PROCESSANDO..."
                    } else {
                        "GERAR BACKUP COMPLETO"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = {
                    seletorBackup.launch(
                        arrayOf(
                            "application/zip",
                            "application/octet-stream",
                            "*/*"
                        )
                    )
                },
                enabled = !uiState.processando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "RESTAURAR BACKUP",
                    color = verde,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "A restauração substitui os dados atuais do aplicativo pelos dados do backup selecionado.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    uriSelecionadoParaRestaurar?.let { uri ->
        AlertDialog(
            onDismissRequest = {
                uriSelecionadoParaRestaurar = null
            },
            title = {
                Text(
                    text = "Restaurar backup?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "A restauração substituirá os dados atuais do RuralColeta pelos dados do arquivo selecionado."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Após restaurar, feche e abra o aplicativo novamente.",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        uriSelecionadoParaRestaurar = null
                        viewModel.restaurarBackup(
                            context = context,
                            backupUri = uri
                        )
                    }
                ) {
                    Text(
                        text = "RESTAURAR",
                        color = vermelho,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        uriSelecionadoParaRestaurar = null
                    }
                ) {
                    Text("CANCELAR")
                }
            }
        )
    }
}
