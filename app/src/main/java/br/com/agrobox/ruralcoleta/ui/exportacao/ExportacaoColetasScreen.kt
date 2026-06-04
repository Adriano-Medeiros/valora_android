package br.com.agrobox.ruralcoleta.ui.exportacao

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExportacaoColetasScreen(
    viewModel: ExportacaoColetasViewModel,
    onBackClick: () -> Unit,
    onExportarClick: (List<Long>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    val todasSelecionadas = uiState.coletas.isNotEmpty() &&
            uiState.selecionadas.size == uiState.coletas.size

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Selecionar coletas",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(48.dp))
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
            Text(
                text = "Escolha as coletas para exportar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Cada coleta selecionada será uma linha na planilha.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = todasSelecionadas,
                    onClick = {
                        if (todasSelecionadas) {
                            viewModel.limparSelecao()
                        } else {
                            viewModel.selecionarTodas()
                        }
                    },
                    label = {
                        Text("Selecionar todas")
                    }
                )

                FilterChip(
                    selected = uiState.selecionadas.isEmpty(),
                    onClick = {
                        viewModel.limparSelecao()
                    },
                    label = {
                        Text("Limpar")
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.coletas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ListAlt,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nenhuma coleta encontrada",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Não existem coletas cadastradas para este formulário.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.coletas) { coleta ->
                    ColetaExportacaoItem(
                        coleta = coleta,
                        selecionada = uiState.selecionadas.contains(coleta.id),
                        onClick = {
                            viewModel.alternarSelecao(coleta.id)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.exportarExcel(context)
                },
                enabled = uiState.selecionadas.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Exportar ${uiState.selecionadas.size} coleta(s)"
                )
            }
        }
    }
}

@Composable
private fun ColetaExportacaoItem(
    coleta: ColetaEntity,
    selecionada: Boolean,
    onClick: () -> Unit
) {
    val tipoTexto = when (coleta.tipoColeta) {
        TipoColeta.AVALIANDO.name -> "Avaliando"
        TipoColeta.AMOSTRAL.name -> "Amostral"
        else -> coleta.tipoColeta
    }

    val statusTexto = if (coleta.status == "RASCUNHO") {
        "Rascunho"
    } else {
        "Concluída"
    }

    val statusFundo = if (coleta.status == "RASCUNHO") {
        Color(0xFFFFE8D6)
    } else {
        Color(0xFFE7F5EC)
    }

    val statusTextoCor = if (coleta.status == "RASCUNHO") {
        Color(0xFFD65F00)
    } else {
        Color(0xFF087B35)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(14.dp),
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = selecionada,
                onCheckedChange = {
                    onClick()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = coleta.nomeReferencia,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "${coleta.municipio}/${coleta.uf} • $tipoTexto",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = formatarData(coleta.dataColeta),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Surface(
                color = statusFundo,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = statusTexto,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    ),
                    color = statusTextoCor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
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