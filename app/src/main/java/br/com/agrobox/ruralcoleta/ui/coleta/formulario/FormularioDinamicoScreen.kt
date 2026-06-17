package br.com.agrobox.ruralcoleta.ui.coleta.formulario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.TipoCampo
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.agrobox.ruralcoleta.ui.components.CampoDataPicker
import br.com.agrobox.ruralcoleta.util.filtrarDecimal

@Composable
fun FormularioDinamicoScreen(
    viewModel: FormularioDinamicoViewModel,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Formulário",
                stepText = "4 de 8",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Variáveis da coleta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (uiState.variaveis.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma variável encontrada para este modelo.",
                        color = Color.Gray
                    )
                }
            }

            items(uiState.variaveis) { variavel ->
                CampoDinamico(
                    variavel = variavel,
                    valor = uiState.respostas[variavel.id] ?: "",
                    opcoes = uiState.opcoesPorVariavel[variavel.id].orEmpty(),
                    temErro = uiState.camposComErro.contains(variavel.id),
                    onValorChange = { valor ->
                        viewModel.alterarResposta(
                            variavelId = variavel.id,
                            valor = valor
                        )
                    }
                )
            }

            item {
                uiState.mensagemErro?.let { mensagem ->
                    Text(
                        text = mensagem,
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Button(
                    onClick = {
                        viewModel.salvarRespostas(
                            onSuccess = onFinishClick
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = verde
                    )
                ) {
                    Text("Salvar respostas")
                }
            }
        }
    }
}

@Composable
private fun CampoDinamico(
    variavel: VariavelEntity,
    valor: String,
    opcoes: List<OpcaoVariavelEntity>,
    temErro: Boolean,
    onValorChange: (String) -> Unit
) {
    when (variavel.tipoCampo) {
        TipoCampo.SIM_NAO.name -> {
            CampoSimNao(
                variavel = variavel,
                valor = valor,
                temErro = temErro,
                onValorChange = onValorChange
            )
        }

        TipoCampo.LISTA.name -> {
            CampoLista(
                variavel = variavel,
                valor = valor,
                opcoes = opcoes,
                temErro = temErro,
                onValorChange = onValorChange
            )
        }
        TipoCampo.NUMERO.name -> {
            OutlinedTextField(
                value = valor,
                onValueChange = { novoValor ->
                    onValorChange(
                        filtrarDecimal(novoValor)
                    )
                },
                label = {
                    Text(
                        text = montarLabel(variavel)
                    )
                },
                isError = temErro,
                supportingText = {
                    if (temErro) {
                        Text("Campo obrigatório")
                    } else {
                        variavel.dica?.let {
                            Text(it)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
        }
        TipoCampo.MOEDA.name -> {
            OutlinedTextField(
                value = valor,
                onValueChange = { novoValor ->
                    onValorChange(
                        filtrarDecimal(novoValor)
                    )
                },
                label = {
                    Text(
                        text = montarLabel(variavel)
                    )
                },
                isError = temErro,
                supportingText = {
                    if (temErro) {
                        Text("Campo obrigatório")
                    } else {
                        variavel.dica?.let {
                            Text(it)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
        }
        TipoCampo.PERCENTUAL.name -> {
            OutlinedTextField(
                value = valor,
                onValueChange = { novoValor ->
                    onValorChange(
                        filtrarDecimal(novoValor)
                    )
                },
                label = {
                    Text(
                        text = montarLabel(variavel)
                    )
                },
                isError = temErro,
                supportingText = {
                    if (temErro) {
                        Text("Campo obrigatório")
                    } else {
                        variavel.dica?.let {
                            Text(it)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
        }
        TipoCampo.DATA.name -> {
            CampoDataPicker(
                valor = valor,
                label = montarLabel(variavel),
                temErro = temErro,
                textoAjuda = variavel.dica ?: "Toque para selecionar a data",
                onValorChange = onValorChange
            )
        }
        else -> {
            OutlinedTextField(
                value = valor,
                onValueChange = onValorChange,
                label = {
                    Text(
                        text = montarLabel(variavel)
                    )
                },
                isError = temErro,
                supportingText = {
                    if (temErro) {
                        Text("Campo obrigatório")
                    } else {
                        variavel.dica?.let {
                            Text(it)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = variavel.tipoCampo != TipoCampo.TEXTO.name
            )
        }
    }
}

@Composable
private fun CampoSimNao(
    variavel: VariavelEntity,
    valor: String,
    temErro: Boolean,
    onValorChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = montarLabel(variavel),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = valor == "SIM",
                onClick = {
                    onValorChange("SIM")
                },
                label = {
                    Text("Sim")
                }
            )

            FilterChip(
                selected = valor == "NAO",
                onClick = {
                    onValorChange("NAO")
                },
                label = {
                    Text("Não")
                }
            )
        }

        if (temErro) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Campo obrigatório",
                color = Color(0xFFD32F2F),
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            variavel.dica?.let {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoLista(
    variavel: VariavelEntity,
    valor: String,
    opcoes: List<OpcaoVariavelEntity>,
    temErro: Boolean,
    onValorChange: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val opcaoSelecionada = opcoes.firstOrNull {
        it.id.toString() == valor
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = opcaoSelecionada?.descricao ?: "",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(montarLabel(variavel))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            isError = temErro,
            supportingText = {
                when {
                    temErro -> {
                        Text("Campo obrigatório")
                    }

                    opcoes.isEmpty() -> {
                        Text("Nenhuma opção cadastrada para esta variável.")
                    }

                    else -> {
                        variavel.dica?.let {
                            Text(it)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = {
                        Text(opcao.descricao)
                    },
                    onClick = {
                        onValorChange(opcao.id.toString())
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun montarLabel(
    variavel: VariavelEntity
): String {
    val obrigatoria = if (variavel.obrigatoria) " *" else ""
    val unidade = variavel.unidade?.let { " ($it)" } ?: ""

    return "${variavel.nome}$unidade$obrigatoria"
}