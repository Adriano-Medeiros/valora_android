package br.com.agrobox.ruralcoleta.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNovaColetaClick: () -> Unit,
    onConfiguracoesClick: () -> Unit,
    onVerTodasClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(verdeEscuro)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(
                        modifier = Modifier.width(48.dp)
                    )

                    Text(
                        text = "Dashboard",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier.width(48.dp)
                    )

                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Resumo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ResumoCard(
                        valor = uiState.totalColetas.toString(),
                        titulo = "Total de Coletas",
                        icone = "📋",
                        modifier = Modifier.weight(1f)
                    )

                    ResumoCard(
                        valor = uiState.coletas.take(5).size.toString(),
                        titulo = "Recentes",
                        icone = "🕘",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ResumoCard(
                        valor = uiState.totalAmostral.toString(),
                        titulo = "Dados Amostrais",
                        icone = "📍",
                        modifier = Modifier.weight(1f)
                    )
                    ResumoCard(
                        valor = uiState.totalAvaliando.toString(),
                        titulo = "Dados Avaliandos",
                        icone = "🌾",
                        modifier = Modifier.weight(1f)
                    )

                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ResumoCard(
                        valor = uiState.totalRascunhos.toString(),
                        titulo = "Rascunhos",
                        icone = "🟠",
                        modifier = Modifier.weight(1f)
                    )

                    ResumoCard(
                        valor = uiState.totalConcluidas.toString(),
                        titulo = "Concluídas",
                        icone = "🟢",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Button(
                    onClick = onNovaColetaClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = verde
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Nova coleta")
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Coletas recentes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = onVerTodasClick) {
                        Text(
                            text = "Ver todas",
                            color = verde
                        )
                    }
                }
            }

            if (uiState.coletas.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nenhuma coleta cadastrada",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Clique em Nova coleta para começar.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            } else {
                items(uiState.coletas.take(5)) { coleta ->
                    ColetaRecenteItem(coleta = coleta)
                }
            }
        }
    }
}

@Composable
private fun ResumoCard(
    valor: String,
    titulo: String,
    icone: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFFEAF5EF),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icone)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = valor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ColetaRecenteItem(
    coleta: ColetaEntity
) {
    val tipoTexto = when (coleta.tipoColeta) {
        TipoColeta.AVALIANDO.name -> "Avaliando"
        TipoColeta.AMOSTRAL.name -> "Amostral"
        else -> coleta.tipoColeta
    }

    val statusColor = if (coleta.tipoColeta == TipoColeta.AVALIANDO.name) {
        Color(0xFFE1F5E8)
    } else {
        Color(0xFFE6F3F8)
    }

    val textColor = if (coleta.tipoColeta == TipoColeta.AVALIANDO.name) {
        Color(0xFF087B35)
    } else {
        Color(0xFF08728A)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = coleta.nomeReferencia,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${coleta.municipio}/${coleta.uf}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )

                Text(
                    text = formatarData(coleta.dataColeta),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    text = if (coleta.status == "RASCUNHO") "Rascunho" else "Concluída",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (coleta.status == "RASCUNHO") {
                        Color(0xFFD65F00)
                    } else {
                        Color(0xFF087B35)
                    }
                )
            }

            Surface(
                color = statusColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = tipoTexto,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
            }
        }
    }
}

private fun formatarData(
    data: Long
): String {
    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale("pt", "BR")
    ).format(Date(data))
}