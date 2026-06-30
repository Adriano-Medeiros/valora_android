package br.com.agrobox.ruralcoleta.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FotoDescricaoDialog(
    titulo: String,
    legendaInicial: String,
    observacaoInicial: String,
    textoBotaoConfirmar: String = "Salvar",
    onConfirmar: (legenda: String, observacao: String) -> Unit,
    onCancelar: () -> Unit
) {
    var legenda by remember(legendaInicial) {
        mutableStateOf(legendaInicial)
    }

    var observacao by remember(observacaoInicial) {
        mutableStateOf(observacaoInicial)
    }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(text = titulo)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = legenda,
                    onValueChange = { legenda = it },
                    label = {
                        Text("Legenda")
                    },
                    placeholder = {
                        Text("Ex.: Vista geral da propriedade")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    label = {
                        Text("Observação")
                    },
                    placeholder = {
                        Text("Ex.: Foto tirada a partir da entrada principal")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmar(
                        legenda.trim(),
                        observacao.trim()
                    )
                }
            ) {
                Text(textoBotaoConfirmar)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCancelar
            ) {
                Text("Cancelar")
            }
        }
    )
}
