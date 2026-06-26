package br.com.agrobox.ruralcoleta.ui.variavel

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.TipoCampo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VariavelFormScreen(
    viewModel: VariavelViewModel,
    onCancelClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    var tipoExpanded by remember { mutableStateOf(false) }
    var grupoExpanded by remember { mutableStateOf(false) }

    val titulo = if (uiState.editando) {
        "Editar variável"
    } else {
        "Nova variável"
    }

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
                        viewModel.salvarVariavel(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    imageVector = Icons.Default.ListAlt,
                    contentDescription = null,
                    tint = verde,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = uiState.nome,
                onValueChange = viewModel::alterarNome,
                label = {
                    Text("Nome da variável *")
                },
                placeholder = {
                    Text("Ex.: Distância do asfalto")
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

            Spacer(modifier = Modifier.height(14.dp))

            ExposedDropdownMenuBox(
                expanded = tipoExpanded,
                onExpandedChange = {
                    tipoExpanded = !tipoExpanded
                }
            ) {
                OutlinedTextField(
                    value = uiState.tipoCampo.name,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Tipo do campo")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = tipoExpanded,
                    onDismissRequest = {
                        tipoExpanded = false
                    }
                ) {
                    TipoCampo.entries.forEach { tipo ->
                        DropdownMenuItem(
                            text = {
                                Text(tipo.name)
                            },
                            onClick = {
                                viewModel.alterarTipoCampo(tipo)
                                tipoExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            ExposedDropdownMenuBox(
                expanded = grupoExpanded,
                onExpandedChange = {
                    grupoExpanded = !grupoExpanded
                }
            ) {
                val grupoSelecionado = uiState.grupos
                    .firstOrNull { it.id == uiState.grupoId }

                OutlinedTextField(
                    value = grupoSelecionado?.nome ?: "Sem grupo",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Grupo")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = grupoExpanded,
                    onDismissRequest = {
                        grupoExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Sem grupo")
                        },
                        onClick = {
                            viewModel.alterarGrupo(null)
                            grupoExpanded = false
                        }
                    )

                    uiState.grupos.forEach { grupo ->
                        DropdownMenuItem(
                            text = {
                                Text(grupo.nome)
                            },
                            onClick = {
                                viewModel.alterarGrupo(grupo.id)
                                grupoExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = uiState.unidade,
                onValueChange = viewModel::alterarUnidade,
                label = {
                    Text("Unidade")
                },
                placeholder = {
                    Text("Ex.: ha, km, R$/ha, %")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = uiState.dica,
                onValueChange = viewModel::alterarDica,
                label = {
                    Text("Dica / descrição")
                },
                placeholder = {
                    Text("Explique como preencher esta variável")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Obrigatória",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Exigir preenchimento na coleta",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = uiState.obrigatoria,
                    onCheckedChange = viewModel::alterarObrigatoria,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = verde
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

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
    }
}
