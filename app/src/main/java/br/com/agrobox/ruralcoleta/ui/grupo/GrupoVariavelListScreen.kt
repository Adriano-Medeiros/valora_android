package br.com.agrobox.ruralcoleta.ui.grupo

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import br.com.agrobox.ruralcoleta.ui.components.CoachMarkOverlay

@Composable
fun GrupoVariavelListScreen(
    viewModel: GrupoVariavelViewModel,
    onBackClick: () -> Unit,
    onNovoGrupoClick: () -> Unit,
    onEditarGrupoClick: (Long) -> Unit,
    mostrarTutorialPrimeiroAcesso: Boolean = false,
    onTutorialProximoClick: () -> Unit = {},
    onTutorialPularClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    val novoGrupoBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            containerColor = fundo,
            topBar = {
                GrupoTopBar(
                    title = "Grupos de variáveis",
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
                        Text("Buscar grupos...")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNovoGrupoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .onGloballyPositioned {
                            novoGrupoBounds.value = it.boundsInRoot()
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

                    Text("Novo grupo")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.grupos) { grupo ->
                        GrupoVariavelItem(
                            grupo = grupo,
                            onClick = {
                                onEditarGrupoClick(grupo.id)
                            }
                        )
                    }
                }
            }
        }

        if (mostrarTutorialPrimeiroAcesso) {
            CoachMarkOverlay(
                targetBounds = novoGrupoBounds.value,
                passoTexto = "Passo 1 de 3",
                titulo = "Toque em Novo grupo",
                descricao = "Aqui você cria o primeiro grupo para organizar as variáveis. Exemplo: Dados do imóvel, Solo, Benfeitorias ou Mercado.",
                textoBotaoPrimario = "Criar grupo",
                onBotaoPrimarioClick = onNovoGrupoClick,
                textoBotaoSecundario = "Próximo",
                onBotaoSecundarioClick = onTutorialProximoClick,
                onPularClick = onTutorialPularClick
            )
        }
    }
}

@Composable
private fun GrupoTopBar(
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
private fun GrupoVariavelItem(
    grupo: GrupoVariavelEntity,
    onClick: () -> Unit
) {
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
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = Color(0xFF00823B)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = grupo.nome,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = grupo.descricao ?: "Sem descrição",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Editar grupo",
                tint = Color.Gray
            )
        }
    }
}
