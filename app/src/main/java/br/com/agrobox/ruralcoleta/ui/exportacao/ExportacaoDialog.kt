package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.domain.exportacao.TipoExportacao

@Composable
fun ExportacaoDialog(
    totalColetas: Int,
    uiState: ExportacaoUiState,
    onDismiss: () -> Unit,
    onExportarClick: (TipoExportacao) -> Unit
) {
    val tipoSelecionado = remember {
        mutableStateOf(TipoExportacao.EXCEL)
    }

    AlertDialog(
        onDismissRequest = {
            if (!uiState.exportando) {
                onDismiss()
            }
        },
        title = {
            Text(
                text = "Exportar coleta",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (totalColetas == 1) {
                        "Escolha o formato para exportar esta coleta."
                    } else {
                        "Escolha o formato para exportar $totalColetas coletas."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(4.dp))

                OpcaoExportacaoItem(
                    titulo = "Excel",
                    descricao = "Planilha com os dados coletados e miniaturas das fotos.",
                    tipo = TipoExportacao.EXCEL,
                    selecionado = tipoSelecionado.value == TipoExportacao.EXCEL,
                    habilitado = !uiState.exportando,
                    onClick = {
                        tipoSelecionado.value = TipoExportacao.EXCEL
                    }
                )

                OpcaoExportacaoItem(
                    titulo = "Pacote completo ZIP",
                    descricao = "Excel + fotos gerais + fotos das benfeitorias organizadas em pastas.",
                    tipo = TipoExportacao.ZIP,
                    selecionado = tipoSelecionado.value == TipoExportacao.ZIP,
                    habilitado = !uiState.exportando,
                    onClick = {
                        tipoSelecionado.value = TipoExportacao.ZIP
                    }
                )

                OpcaoExportacaoItem(
                    titulo = "Relatório PDF",
                    descricao = "Será implementado em uma próxima versão.",
                    tipo = TipoExportacao.PDF,
                    selecionado = false,
                    habilitado = false,
                    onClick = {}
                )

                uiState.mensagemErro?.let { mensagem ->
                    Text(
                        text = mensagem,
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onExportarClick(tipoSelecionado.value)
                },
                enabled = !uiState.exportando,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (uiState.exportando) {
                        when (uiState.tipoExportando) {
                            TipoExportacao.EXCEL -> "Gerando Excel..."
                            TipoExportacao.ZIP -> "Gerando ZIP..."
                            TipoExportacao.PDF -> "Gerando PDF..."
                            null -> "Exportando..."
                        }
                    } else {
                        "Exportar"
                    }
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !uiState.exportando
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun OpcaoExportacaoItem(
    titulo: String,
    descricao: String,
    tipo: TipoExportacao,
    selecionado: Boolean,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    val icone = when (tipo) {
        TipoExportacao.EXCEL -> Icons.Default.Description
        TipoExportacao.ZIP -> Icons.Default.Archive
        TipoExportacao.PDF -> Icons.Default.PictureAsPdf
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selecionado,
                enabled = habilitado,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selecionado,
            onClick = onClick,
            enabled = habilitado
        )

        Icon(
            imageVector = icone,
            contentDescription = null,
            tint = if (habilitado) {
                Color(0xFF00823B)
            } else {
                Color.LightGray
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                color = if (habilitado) {
                    Color(0xFF222222)
                } else {
                    Color.Gray
                }
            )

            Text(
                text = descricao,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
