package br.com.agrobox.ruralcoleta.ui.modelo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import br.com.agrobox.ruralcoleta.ui.components.CoachMarkOverlay
@Composable
fun ModeloColetaListScreen(
    viewModel: ModeloColetaViewModel,
    onBackClick: () -> Unit,
    onNovoModeloClick: () -> Unit,
    mostrarTutorialPrimeiroAcesso: Boolean = false,
    onTutorialFinalizarClick: () -> Unit = {},
    onTutorialPularClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)
    val novoFormularioBounds = remember {
        mutableStateOf<Rect?>(null)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    )
    Scaffold(
        containerColor = fundo,
        topBar = {
            ModeloTopBar(
                title = "Formulário de pesquisa",
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
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = {
                    Text("Buscar formulários...")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNovoModeloClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .onGloballyPositioned {
                        novoFormularioBounds.value = it.boundsInRoot()
                    },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Novo formulário")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.modelos) { modelo ->
                    ModeloColetaItem(
                        modelo = modelo
                    )
                }
            }
        }
    }
    if (mostrarTutorialPrimeiroAcesso) {
        CoachMarkOverlay(
            targetBounds = novoFormularioBounds.value,
            passoTexto = "Passo 3 de 3",
            titulo = "Toque em Novo formulário",
            descricao = "Agora você monta o formulário de pesquisa escolhendo quais variáveis serão usadas durante a coleta.",
            textoBotaoPrimario = "Criar formulário",
            onBotaoPrimarioClick = onNovoModeloClick,
            textoBotaoSecundario = "Finalizar",
            onBotaoSecundarioClick = onTutorialFinalizarClick,
            onPularClick = onTutorialPularClick
        )
    }
}

@Composable
private fun ModeloTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    val verdeEscuro = Color(0xFF003B24)

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
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
private fun ModeloColetaItem(
    modelo: ModeloColetaEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFFE7F5EC),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DynamicForm,
                    contentDescription = null,
                    tint = Color(0xFF00823B)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = modelo.nome,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = modelo.descricao ?: "Sem descrição",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}