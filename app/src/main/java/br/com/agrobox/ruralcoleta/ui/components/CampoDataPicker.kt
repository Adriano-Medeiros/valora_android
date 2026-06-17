package br.com.agrobox.ruralcoleta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDataPicker(
    valor: String,
    label: String,
    modifier: Modifier = Modifier,
    temErro: Boolean = false,
    mensagemErro: String = "Campo obrigatório",
    textoAjuda: String = "Toque para selecionar a data",
    onValorChange: (String) -> Unit
) {
    val mostrarDatePicker = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(label)
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Selecionar data"
                )
            },
            isError = temErro,
            supportingText = {
                if (temErro) {
                    Text(mensagemErro)
                } else {
                    Text(textoAjuda)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    mostrarDatePicker.value = true
                }
        )
    }

    if (mostrarDatePicker.value) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = textoDataParaMillis(valor)
        )

        DatePickerDialog(
            onDismissRequest = {
                mostrarDatePicker.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val dataSelecionada = datePickerState.selectedDateMillis

                        if (dataSelecionada != null) {
                            onValorChange(
                                millisParaTextoData(dataSelecionada)
                            )
                        }

                        mostrarDatePicker.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDatePicker.value = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Selecionar data",
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = 16.dp
                        )
                    )
                },
                headline = {
                    Text(
                        text = "Escolha uma data",
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 8.dp
                        )
                    )
                }
            )
        }
    }
}

private fun millisParaTextoData(
    millis: Long
): String {
    val data = Instant
        .ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    return data.format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )
}

private fun textoDataParaMillis(
    texto: String
): Long? {
    if (texto.isBlank()) {
        return null
    }

    return try {
        val data = LocalDate.parse(
            texto,
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )

        data
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    } catch (erro: DateTimeParseException) {
        null
    }
}