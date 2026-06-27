package br.com.agrobox.ruralcoleta.ui.modelo

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

    val titulo = if (uiState.editando) {
        "Editar formulário"
    } else {
        "Novo formulário"
    }

    val variaveisOrdenadasParaExibicao = ordenarVariaveisParaExibicao(
        variaveis = uiState.variaveis,
        selecionadas = uiState.variaveisSelecionadas
    )

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
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                TextButton(
                    onClick = {
                        viewModel.salvarModelo(
                            onSuccess = onSaveSuccess
                        )
                    },
                    enabled = !uiState.salvando
                ) {
                    Text(
                        text = if (uiState.salvando) "Salvando..." else "Salvar",
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
                    singleLine = true,
                    isError = uiState.mensagemErro != null,
                    supportingText = {
                        uiState.mensagemErro?.let { mensagem ->
                            Text(mensagem)
                        }
                    }
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Variáveis para pesquisa",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Marque as variáveis na ordem desejada. A numeração indica a ordem em que elas aparecerão na coleta.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            items(
                items = variaveisOrdenadasParaExibicao,
                key = { variavel ->
                    variavel.id
                }
            ) { variavel ->

                val ordem = uiState.variaveisSelecionadas.indexOf(variavel.id) + 1
                val selecionada = ordem > 0

                VariavelSelecionavelItem(
                    variavel = variavel,
                    selecionada = selecionada,
                    ordem = if (selecionada) ordem else null,
                    podeSubir = ordem > 1,
                    podeDescer = selecionada && ordem < uiState.variaveisSelecionadas.size,
                    onClick = {
                        viewModel.alternarVariavelSelecionada(variavel.id)
                    },
                    onSubirClick = {
                        viewModel.moverVariavelParaCima(variavel.id)
                    },
                    onDescerClick = {
                        viewModel.moverVariavelParaBaixo(variavel.id)
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
    ordem: Int?,
    podeSubir: Boolean,
    podeDescer: Boolean,
    onClick: () -> Unit,
    onSubirClick: () -> Unit,
    onDescerClick: () -> Unit
) {
    val verde = Color(0xFF00823B)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selecionada) {
                Color(0xFFEAF6EF)
            } else {
                Color.White
            }
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

            if (ordem != null) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = verde,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ordem.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
            }

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

                if (selecionada) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Ordem no formulário: $ordem",
                        color = verde,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (selecionada) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = onSubirClick,
                        enabled = podeSubir
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Subir variável",
                            tint = if (podeSubir) verde else Color.LightGray
                        )
                    }

                    IconButton(
                        onClick = onDescerClick,
                        enabled = podeDescer
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Descer variável",
                            tint = if (podeDescer) verde else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

private fun ordenarVariaveisParaExibicao(
    variaveis: List<VariavelEntity>,
    selecionadas: List<Long>
): List<VariavelEntity> {
    val mapaOrdem = selecionadas
        .mapIndexed { index, id ->
            id to index
        }
        .toMap()

    return variaveis.sortedWith(
        compareBy<VariavelEntity> { variavel ->
            !selecionadas.contains(variavel.id)
        }.thenBy { variavel ->
            mapaOrdem[variavel.id] ?: Int.MAX_VALUE
        }.thenBy { variavel ->
            variavel.nome
        }
    )
}