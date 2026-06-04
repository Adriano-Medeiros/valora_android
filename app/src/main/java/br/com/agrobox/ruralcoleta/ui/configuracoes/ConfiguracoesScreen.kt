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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfiguracoesScreen(
    onBackClick: () -> Unit,
    onGruposClick: () -> Unit,
    onVariaveisClick: () -> Unit,
    onModelosClick: () -> Unit,
    onPreferenciasClick: () -> Unit,
    onSobreAppClick: () -> Unit
) {
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
                text = "Configure os grupos, variáveis e modelos usados nos formulários de coleta.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )

            ConfiguracaoItem(
                titulo = "Grupos de variáveis",
                descricao = "Organize os campos por seções, como localização, mercado e benfeitorias.",
                icon = Icons.Default.Folder,
                iconColor = verde,
                onClick = onGruposClick
            )

            ConfiguracaoItem(
                titulo = "Variáveis",
                descricao = "Cadastre os campos que serão usados nas coletas.",
                icon = Icons.Default.ListAlt,
                iconColor = verde,
                onClick = onVariaveisClick
            )

            ConfiguracaoItem(
                titulo = "Formulário de pesquisa",
                descricao = "Monte os formulários usando as variáveis cadastradas.",
                icon = Icons.Default.DynamicForm,
                iconColor = verde,
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
}

@Composable
private fun ConfiguracaoItem(
    titulo: String,
    descricao: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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