package br.com.agrobox.ruralcoleta.ui.configuracoes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.ui.components.CoachMarkOverlay

@Composable
fun ConfiguracoesScreen(
    onBackClick: () -> Unit,
    onGruposClick: () -> Unit,
    onVariaveisClick: () -> Unit,
    onModelosClick: () -> Unit,
    onPreferenciasClick: () -> Unit,
    onSobreAppClick: () -> Unit,
    mostrarTutorialPrimeiroAcesso: Boolean = false,
    tutorialEtapa: Int = 0,
    onTutorialAbrirGruposClick: () -> Unit = {},
    onTutorialAbrirVariaveisClick: () -> Unit = {},
    onTutorialAbrirModelosClick: () -> Unit = {},
    onTutorialPularClick: () -> Unit = {}
) {
    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    val gruposBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    val variaveisBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    val modelosBounds = remember {
        mutableStateOf<Rect?>(null)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                                text = "Configurações",
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Estrutura da coleta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F1F)
                )

                Text(
                    text = "Configure os grupos, variáveis e formulários usados nas coletas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )

                ConfiguracaoItem(
                    titulo = "Grupos de variáveis",
                    descricao = "Organize os campos por seções, como localização, solo, mercado e benfeitorias.",
                    icon = Icons.Default.Folder,
                    iconColor = verde,
                    modifier = Modifier.onGloballyPositioned {
                        gruposBounds.value = it.boundsInRoot()
                    },
                    onClick = onGruposClick
                )

                ConfiguracaoItem(
                    titulo = "Variáveis",
                    descricao = "Cadastre os campos que serão preenchidos nas coletas.",
                    icon = Icons.Default.ListAlt,
                    iconColor = verde,
                    modifier = Modifier.onGloballyPositioned {
                        variaveisBounds.value = it.boundsInRoot()
                    },
                    onClick = onVariaveisClick
                )

                ConfiguracaoItem(
                    titulo = "Formulário de pesquisa",
                    descricao = "Monte os formulários escolhendo as variáveis que farão parte da coleta.",
                    icon = Icons.Default.DynamicForm,
                    iconColor = verde,
                    modifier = Modifier.onGloballyPositioned {
                        modelosBounds.value = it.boundsInRoot()
                    },
                    onClick = onModelosClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sistema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F1F)
                )

                ConfiguracaoItem(
                    titulo = "Preferências",
                    descricao = "Ajustes gerais do aplicativo.",
                    icon = Icons.Default.Tune,
                    iconColor = verde,
                    onClick = onPreferenciasClick
                )

                ConfiguracaoItem(
                    titulo = "Sobre o app",
                    descricao = "Informações sobre o RuralColeta.",
                    icon = Icons.Default.Settings,
                    iconColor = verde,
                    onClick = onSobreAppClick
                )
            }
        }

        if (mostrarTutorialPrimeiroAcesso) {
            when (tutorialEtapa) {
                0 -> {
                    CoachMarkOverlay(
                        targetBounds = gruposBounds.value,
                        passoTexto = "Passo 1 de 3",
                        titulo = "Primeiro crie um grupo",
                        descricao = "Os grupos servem para organizar as variáveis. Exemplo: Dados do imóvel, Solo, Benfeitorias ou Mercado.",
                        textoBotaoPrimario = "Abrir grupos",
                        onBotaoPrimarioClick = onTutorialAbrirGruposClick,
                        onPularClick = onTutorialPularClick
                    )
                }

                2 -> {
                    CoachMarkOverlay(
                        targetBounds = variaveisBounds.value,
                        passoTexto = "Passo 2 de 3",
                        titulo = "Depois crie as variáveis",
                        descricao = "As variáveis são os campos que o usuário preencherá durante a coleta. Exemplo: área total, tipo de solo, acesso, energia e água.",
                        textoBotaoPrimario = "Abrir variáveis",
                        onBotaoPrimarioClick = onTutorialAbrirVariaveisClick,
                        onPularClick = onTutorialPularClick
                    )
                }

                4 -> {
                    CoachMarkOverlay(
                        targetBounds = modelosBounds.value,
                        passoTexto = "Passo 3 de 3",
                        titulo = "Por fim crie o formulário",
                        descricao = "O formulário de pesquisa junta as variáveis cadastradas e define quais campos serão usados na coleta.",
                        textoBotaoPrimario = "Abrir formulários",
                        onBotaoPrimarioClick = onTutorialAbrirModelosClick,
                        onPularClick = onTutorialPularClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfiguracaoItem(
    titulo: String,
    descricao: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
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
                    .size(44.dp)
                    .background(
                        color = Color(0xFFE7F5EC),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1F1F1F)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}