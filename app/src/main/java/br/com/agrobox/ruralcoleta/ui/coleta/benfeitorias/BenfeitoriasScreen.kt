package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.CategoriaBenfeitoria
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import br.com.agrobox.ruralcoleta.ui.components.ConfirmDeleteDialog
@Composable
fun BenfeitoriasScreen(
    viewModel: BenfeitoriasViewModel,
    onBackClick: () -> Unit,
    onAdicionarClick: () -> Unit,
    onFotosClick: (Long) -> Unit,
    onNextClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val benfeitoriaParaExcluir = remember {
        mutableStateOf<BenfeitoriaEntity?>(null)
    }

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    benfeitoriaParaExcluir.value?.let { benfeitoria ->

        ConfirmDeleteDialog(
            title = "Excluir benfeitoria",
            message = "Deseja realmente excluir esta benfeitoria?",
            onConfirm = {
                viewModel.excluirBenfeitoria(benfeitoria)
                benfeitoriaParaExcluir.value = null
            },
            onDismiss = {
                benfeitoriaParaExcluir.value = null
            }
        )
    }
    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Benfeitorias",
                stepText = "5 de 8",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Benfeitorias da coleta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Etapa opcional. Cadastre benfeitorias reprodutivas e não reprodutivas, se houver.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAdicionarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
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

                Text("Adicionar benfeitoria")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.benfeitorias.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Landscape,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nenhuma benfeitoria cadastrada",
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Você pode avançar sem cadastrar.",
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
                items(uiState.benfeitorias) { benfeitoria ->
                    BenfeitoriaItem(
                        benfeitoria = benfeitoria,
                        onFotosClick = {
                            onFotosClick(benfeitoria.id)
                        },
                        onDeleteClick = {
                            benfeitoriaParaExcluir.value = benfeitoria
                        }
                    )
                }
            }

            Button(
                onClick = onNextClick,
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

@Composable
private fun BenfeitoriaItem(
    benfeitoria: BenfeitoriaEntity,
    onFotosClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = Color(0xFFE7F5EC),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Landscape,
                    contentDescription = null,
                    tint = Color(0xFF00823B)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = benfeitoria.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (benfeitoria.categoria == CategoriaBenfeitoria.REPRODUTIVA.name) {
                        "Reprodutiva"
                    } else {
                        "Não reprodutiva"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                benfeitoria.estadoConservacao?.let {
                    Text(
                        text = "Estado: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            IconButton(
                onClick = onFotosClick
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Fotos",
                    tint = Color.Gray
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = Color(0xFFD32F2F)
                )
            }
        }
    }
}