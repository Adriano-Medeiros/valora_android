package br.com.agrobox.ruralcoleta.ui.modelo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

@Composable
fun ModeloColetaFormScreen(
    viewModel: ModeloColetaViewModel,
    onCancelClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onCancelClick
                ) {
                    Text(
                        text = "Cancelar",
                        color = verde
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Novo formulário pesquisa",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                TextButton(
                    onClick = {
                        viewModel.salvarModelo(
                            onSuccess = onSaveSuccess
                        )
                    }
                ) {
                    Text(
                        text = "Salvar",
                        color = verde
                    )
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(
                            color = Color(0xFFE7F5EC),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DynamicForm,
                        contentDescription = null,
                        tint = verde,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = uiState.nome,
                    onValueChange = viewModel::alterarNome,
                    label = {
                        Text("Nome do formulário *")
                    },
                    placeholder = {
                        Text("Ex.: Coleta rural padrão")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.descricao,
                    onValueChange = viewModel::alterarDescricao,
                    label = {
                        Text("Descrição")
                    },
                    placeholder = {
                        Text("Descreva o objetivo deste formulário")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ativo",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Switch(
                        checked = uiState.ativo,
                        onCheckedChange = viewModel::alterarAtivo,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = verde
                        )
                    )
                }
            }

            item {
                Divider()
            }

            item {
                Text(
                    text = "Variáveis para pesquisa",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(uiState.variaveis) { variavel ->
                VariavelSelecionavelItem(
                    variavel = variavel,
                    selecionada = uiState.variaveisSelecionadas.contains(variavel.id),
                    onClick = {
                        viewModel.alternarVariavelSelecionada(variavel.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun VariavelSelecionavelItem(
    variavel: VariavelEntity,
    selecionada: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
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
                    text = variavel.nome,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${variavel.tipoCampo}${variavel.unidade?.let { " • $it" } ?: ""}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}