package br.com.agrobox.ruralcoleta.ui.coleta.detalhe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaDetalhada
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import br.com.agrobox.ruralcoleta.ui.components.ConfirmDeleteDialog
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaComFotos
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
@Composable
fun DetalheColetaScreen(
    viewModel: DetalheColetaViewModel,
    onBackClick: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onContinueOrEditClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val coleta = uiState.coleta

    val mostrarDialogExcluir = remember {
        mutableStateOf(false)
    }

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    if (mostrarDialogExcluir.value) {
        ConfirmDeleteDialog(
            title = "Excluir coleta",
            message = "Deseja realmente excluir esta coleta? Esta ação não poderá ser desfeita.",
            onConfirm = {
                mostrarDialogExcluir.value = false
                viewModel.excluirColeta(
                    onSuccess = onDeleteSuccess
                )
            },
            onDismiss = {
                mostrarDialogExcluir.value = false
            }
        )
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
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
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
                            text = "Detalhes da coleta",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = {
                            mostrarDialogExcluir.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir coleta",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        if (coleta == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Coleta não encontrada")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    HeaderColetaCard(
                        nome = coleta.nomeReferencia,
                        municipio = coleta.municipio,
                        uf = coleta.uf,
                        tipo = coleta.tipoColeta,
                        data = coleta.dataColeta
                    )
                }
                item {
                    Button(
                        onClick = {
                            onContinueOrEditClick(coleta.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00823B)
                        )
                    ) {
                        Text(
                            text = if (coleta.status == "RASCUNHO") {
                                "Continuar rascunho"
                            } else {
                                "Editar coleta"
                            }
                        )
                    }
                }
                item {
                    SecaoResumoCard(
                        titulo = "Respostas",
                        icone = {
                            Icon(
                                imageVector = Icons.Default.QuestionAnswer,
                                contentDescription = null,
                                tint = verde
                            )
                        },
                        linhas = if (uiState.respostas.isEmpty()) {
                            listOf("Nenhuma resposta registrada")
                        } else {
                            uiState.respostas.map { resposta ->
                                "${resposta.nomeVariavel}: ${formatarResposta(resposta)}"
                            }
                        }
                    )
                }

                item {
                    BenfeitoriasResumoCard(
                        benfeitorias = uiState.benfeitorias,
                        fotosPorBenfeitoria = uiState.fotosPorBenfeitoria
                    )
                }

                item {
                    FotosResumoCard(
                        fotos = uiState.fotos
                    )
                }

                item {
                    SecaoResumoCard(
                        titulo = "Observações",
                        icone = {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = verde
                            )
                        },
                        linhas = listOf(
                            coleta.observacao ?: "Sem observações"
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderColetaCard(
    nome: String,
    municipio: String,
    uf: String,
    tipo: String,
    data: Long
) {
    val tipoTexto = when (tipo) {
        TipoColeta.AVALIANDO.name -> "Imóvel avaliando"
        TipoColeta.AMOSTRAL.name -> "Dado amostral"
        else -> tipo
    }

    val tipoColor = if (tipo == TipoColeta.AVALIANDO.name) {
        Color(0xFF087B35)
    } else {
        Color(0xFF08728A)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = nome,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$municipio/$uf",
                color = Color.DarkGray
            )

            Text(
                text = formatarData(data),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Surface(
                color = Color(0xFFE7F5EC),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = tipoTexto,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    color = tipoColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun SecaoResumoCard(
    titulo: String,
    icone: @Composable () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = Color(0xFFE7F5EC),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    icone()
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            linhas.forEach { linha ->
                Text(
                    text = linha,
                    color = Color(0xFF444444),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun FotosResumoCard(
    fotos: List<FotoColetaEntity>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = Color(0xFFE7F5EC),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = null,
                        tint = Color(0xFF00823B)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Fotos gerais",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (fotos.isEmpty()) {
                Text(
                    text = "Nenhuma foto registrada",
                    color = Color(0xFF444444),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.heightIn(max = 420.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false
                ) {
                    items(fotos) { foto ->
                        Column {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = File(foto.caminhoArquivo)
                                ),
                                contentDescription = foto.legenda,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = foto.legenda ?: "Sem legenda",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatarResposta(
    resposta: RespostaDetalhada
): String {
    val valor = when {
        resposta.valorTexto != null -> resposta.valorTexto
        resposta.valorNumero != null -> resposta.valorNumero.toString()
        resposta.valorBooleano != null -> if (resposta.valorBooleano) "Sim" else "Não"
        resposta.valorData != null -> formatarData(resposta.valorData)
        else -> "-"
    }

    val unidade = resposta.unidade?.let { " $it" } ?: ""

    return "$valor$unidade"
}

private fun formatarData(
    data: Long
): String {
    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale("pt", "BR")
    ).format(Date(data))
}
@Composable
private fun BenfeitoriasResumoCard(
    benfeitorias: List<BenfeitoriaComFotos>,
    fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = Color(0xFFE7F5EC),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HomeRepairService,
                        contentDescription = null,
                        tint = Color(0xFF00823B)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Benfeitorias",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (benfeitorias.isEmpty()) {
                Text(
                    text = "Nenhuma benfeitoria cadastrada",
                    color = Color(0xFF444444),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                benfeitorias.forEach { benfeitoria ->
                    val categoria = if (benfeitoria.categoria == "REPRODUTIVA") {
                        "Reprodutiva"
                    } else {
                        "Não reprodutiva"
                    }

                    val quantidade = benfeitoria.quantidade?.let {
                        " • ${it}${benfeitoria.unidade?.let { unidade -> " $unidade" } ?: ""}"
                    } ?: ""

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = benfeitoria.nome,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "$categoria$quantidade",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )

                        benfeitoria.estadoConservacao?.let {
                            Text(
                                text = "Estado: $it",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        benfeitoria.observacao?.let {
                            Text(
                                text = it,
                                color = Color(0xFF444444),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        val fotos = fotosPorBenfeitoria[benfeitoria.id].orEmpty()

                        Spacer(modifier = Modifier.height(8.dp))

                        if (fotos.isEmpty()) {
                            Text(
                                text = "Sem fotos da benfeitoria",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.heightIn(max = 320.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                userScrollEnabled = false
                            ) {
                                items(fotos) { foto ->
                                    Column {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                model = File(foto.caminhoArquivo)
                                            ),
                                            contentDescription = foto.legenda,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(120.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )

                                        Text(
                                            text = foto.legenda ?: "Sem legenda",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Divider(
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}