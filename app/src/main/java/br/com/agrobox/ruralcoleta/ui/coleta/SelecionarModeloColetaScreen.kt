package br.com.agrobox.ruralcoleta.ui.coleta

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DynamicForm
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
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar
import br.com.agrobox.ruralcoleta.ui.modelo.ModeloColetaViewModel

@Composable
fun SelecionarModeloColetaScreen(
    novaColetaViewModel: NovaColetaViewModel,
    modeloColetaViewModel: ModeloColetaViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val novaColetaState by novaColetaViewModel.uiState.collectAsState()
    val modeloState by modeloColetaViewModel.uiState.collectAsState()

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Modelo de coleta",
                stepText = "2 de 8",
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
                text = "Selecione o modelo do formulário",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "O modelo define quais variáveis serão preenchidas nesta coleta.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(modeloState.modelos) { modelo ->
                    ModeloSelecionavelItem(
                        modelo = modelo,
                        selecionado = novaColetaState.modeloColetaId == modelo.id,
                        onClick = {
                            novaColetaViewModel.alterarModelo(modelo.id)
                        }
                    )
                }
            }

            Button(
                onClick = onNextClick,
                enabled = novaColetaState.modeloColetaId != null,
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
private fun ModeloSelecionavelItem(
    modelo: ModeloColetaEntity,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val verde = Color(0xFF00823B)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = if (selecionado) {
            ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.5.dp,
                brush = androidx.compose.ui.graphics.SolidColor(verde)
            )
        } else {
            null
        }
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
                    imageVector = Icons.Default.DynamicForm,
                    contentDescription = null,
                    tint = verde
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = modelo.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = modelo.descricao ?: "Sem descrição",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (selecionado) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = verde
                )
            }
        }
    }
}