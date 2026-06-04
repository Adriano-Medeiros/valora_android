package br.com.agrobox.ruralcoleta.ui.coleta.resumo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar

@Composable
fun ResumoColetaScreen(
    viewModel: ResumoColetaViewModel,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Resumo",
                stepText = "8 de 8",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = verde,
                        modifier = Modifier.size(58.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Coleta registrada",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Confira o resumo antes de finalizar.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            item {
                ResumoCard(
                    titulo = "Dados gerais",
                    linhas = listOf(
                        "Nome: ${uiState.coleta?.nomeReferencia ?: "-"}",
                        "Município: ${uiState.coleta?.municipio ?: "-"}",
                        "UF: ${uiState.coleta?.uf ?: "-"}",
                        "Tipo: ${uiState.coleta?.tipoColeta ?: "-"}"
                    )
                )
            }

            item {
                ResumoCard(
                    titulo = "Itens registrados",
                    linhas = listOf(
                        "Respostas: ${uiState.respostas.size}",
                        "Benfeitorias: ${uiState.benfeitorias.size}",
                        "Fotos gerais: ${uiState.fotos.size}"
                    )
                )
            }

            item {
                ResumoCard(
                    titulo = "Observação geral",
                    linhas = listOf(
                        uiState.coleta?.observacao ?: "Sem observações"
                    )
                )
            }

            item {
                uiState.mensagemErro?.let { mensagem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFE8D6)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = mensagem,
                                color = Color(0xFFD65F00),
                                fontWeight = FontWeight.Bold
                            )

                            uiState.pendencias.forEach { pendencia ->
                                Text(
                                    text = "• $pendencia",
                                    color = Color(0xFF6B3A00),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        viewModel.finalizarColeta(
                            onSuccess = onFinishClick
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
                    Text("Finalizar")
                }
            }
        }
    }
}

@Composable
private fun ResumoCard(
    titulo: String,
    linhas: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            linhas.forEach { linha ->
                Text(
                    text = linha,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF444444)
                )
            }
        }
    }
}