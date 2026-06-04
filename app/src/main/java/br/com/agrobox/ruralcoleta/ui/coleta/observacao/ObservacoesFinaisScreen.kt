package br.com.agrobox.ruralcoleta.ui.coleta.observacoes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar

@Composable
fun ObservacoesFinaisScreen(
    viewModel: ObservacoesFinaisViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Observações",
                stepText = "7 de 8",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notes,
                contentDescription = null,
                tint = verde,
                modifier = Modifier.size(42.dp)
            )

            Text(
                text = "Observações finais",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Registre observações gerais sobre a coleta, informações complementares ou pontos de atenção.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = viewModel::alterarObservacao,
                label = {
                    Text("Observação geral")
                },
                placeholder = {
                    Text("Digite as observações finais da coleta")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp),
                minLines = 8
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.salvar(
                        onSuccess = onNextClick
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Text("Próximo")
            }
        }
    }
}