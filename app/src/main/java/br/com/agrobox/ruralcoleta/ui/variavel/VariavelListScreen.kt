package br.com.agrobox.ruralcoleta.ui.variavel

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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import br.com.agrobox.ruralcoleta.data.local.entity.TipoCampo
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import br.com.agrobox.ruralcoleta.ui.components.CoachMarkOverlay

@Composable
fun VariavelListScreen(
    viewModel: VariavelViewModel,
    onBackClick: () -> Unit,
    onNovaVariavelClick: () -> Unit,
    onEditarVariavelClick: (Long) -> Unit,
    onOpcoesClick: (Long) -> Unit,
    mostrarTutorialPrimeiroAcesso: Boolean = false,
    onTutorialProximoClick: () -> Unit = {},
    onTutorialPularClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    val novaVariavelBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            containerColor = fundo,
            topBar = {
                VariavelTopBar(
                    title = "Variáveis",
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
                        Text("Buscar variáveis...")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNovaVariavelClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .onGloballyPositioned {
                            novaVariavelBounds.value = it.boundsInRoot()
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

                    Text("Nova variável")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.variaveis) { variavel ->
                        VariavelItem(
                            variavel = variavel,
                            onEditarClick = {
                                onEditarVariavelClick(variavel.id)
                            },
                            onOpcoesClick = {
                                onOpcoesClick(variavel.id)
                            }
                        )
                    }
                }
            }
        }

        if (mostrarTutorialPrimeiroAcesso) {
            CoachMarkOverlay(
                targetBounds = novaVariavelBounds.value,
                passoTexto = "Passo 2 de 3",
                titulo = "Toque em Nova variável",
                descricao = "Aqui você cria os campos que serão preenchidos na coleta. Exemplo: área total, tipo de solo, acesso, energia, água ou valor observado.",
                textoBotaoPrimario = "Criar variável",
                onBotaoPrimarioClick = onNovaVariavelClick,
                textoBotaoSecundario = "Próximo",
                onBotaoSecundarioClick = onTutorialProximoClick,
                onPularClick = onTutorialPularClick
            )
        }
    }
}

@Composable
private fun VariavelTopBar(
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
private fun VariavelItem(
    variavel: VariavelEntity,
    onEditarClick: () -> Unit,
    onOpcoesClick: () -> Unit
) {
    val possuiOpcoes = variavel.tipoCampo == TipoCampo.LISTA.name

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEditarClick()
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
                    imageVector = Icons.Default.ListAlt,
                    contentDescription = null,
                    tint = Color(0xFF00823B)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = variavel.nome,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "${variavel.tipoCampo}${variavel.unidade?.let { " • $it" } ?: ""}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                if (possuiOpcoes) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        color = Color(0xFFE7F5EC),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Possui opções",
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),
                            color = Color(0xFF087B35),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                TextButton(
                    onClick = onEditarClick
                ) {
                    Text("Editar")
                }

                if (possuiOpcoes) {
                    TextButton(
                        onClick = onOpcoesClick
                    ) {
                        Text("Opções")
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Editar variável",
                tint = Color.Gray
            )
        }
    }
}
