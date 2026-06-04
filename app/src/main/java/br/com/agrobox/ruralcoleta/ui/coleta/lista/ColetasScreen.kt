package br.com.agrobox.ruralcoleta.ui.coleta.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import br.com.agrobox.ruralcoleta.ui.dashboard.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ColetasScreen(
    viewModel: DashboardViewModel,
    onNovaColetaClick: () -> Unit,
    onColetaClick: (Long) -> Unit,
    onExportarClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var filtro by remember { mutableStateOf("TODAS") }

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    val coletasFiltradas = when (filtro) {
        "AVALIANDO" -> uiState.coletas.filter { it.tipoColeta == TipoColeta.AVALIANDO.name }
        "AMOSTRAL" -> uiState.coletas.filter { it.tipoColeta == TipoColeta.AMOSTRAL.name }
        else -> uiState.coletas
    }

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
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Coletas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filtro == "TODAS",
                    onClick = { filtro = "TODAS" },
                    label = { Text("Todas") }
                )

                FilterChip(
                    selected = filtro == "AVALIANDO",
                    onClick = { filtro = "AVALIANDO" },
                    label = { Text("Avaliando") }
                )

                FilterChip(
                    selected = filtro == "AMOSTRAL",
                    onClick = { filtro = "AMOSTRAL" },
                    label = { Text("Amostral") }
                )
            }

//            Spacer(modifier = Modifier.height(12.dp))
//
//            Button(
//                onClick = onNovaColetaClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = verde
//                )
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = null
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Text("Nova coleta")
//            }

            Spacer(modifier = Modifier.height(16.dp))

            if (coletasFiltradas.isEmpty()) {
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
                        Icon(
                            imageVector = Icons.Default.ListAlt,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nenhuma coleta encontrada",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Cadastre uma nova coleta para começar.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNovaColetaClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = verde
                    )
                ) {
                    Text("Nova coleta")
                }

                OutlinedButton(
                    onClick = onExportarClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Exportar")
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(coletasFiltradas) { coleta ->
                    ColetaListaItem(
                        coleta = coleta,
                        onClick = {
                            onColetaClick(coleta.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColetaListaItem(
    coleta: ColetaEntity,
    onClick: () -> Unit
) {
    val tipoTexto = when (coleta.tipoColeta) {
        TipoColeta.AVALIANDO.name -> "Avaliando"
        TipoColeta.AMOSTRAL.name -> "Amostral"
        else -> coleta.tipoColeta
    }

    val tipoColor = if (coleta.tipoColeta == TipoColeta.AVALIANDO.name) {
        Color(0xFF087B35)
    } else {
        Color(0xFF08728A)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = coleta.nomeReferencia,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${coleta.municipio}/${coleta.uf}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Text(
                        text = formatarData(coleta.dataColeta),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (coleta.status == "RASCUNHO") {
                        Color(0xFFFFE8D6)
                    } else {
                        Color(0xFFE7F5EC)
                    }
                ) {

                    Text(
                        text = if (coleta.status == "RASCUNHO") {
                            "Rascunho"
                        } else {
                            "Concluída"
                        },

                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),

                        color = if (coleta.status == "RASCUNHO") {
                            Color(0xFFD65F00)
                        } else {
                            Color(0xFF087B35)
                        },

                        style = MaterialTheme.typography.labelMedium,

                        fontWeight = FontWeight.Bold
                    )
                }
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