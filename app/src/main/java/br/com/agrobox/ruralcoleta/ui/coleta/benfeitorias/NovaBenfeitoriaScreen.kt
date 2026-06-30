package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.CategoriaBenfeitoria
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar

@Composable
fun NovaBenfeitoriaScreen(
    viewModel: BenfeitoriasViewModel,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    onFotosClick: ((Long) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = if (uiState.editando) {
                    "Editar benfeitoria"
                } else {
                    "Nova benfeitoria"
                },
                stepText = "5 de 8",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        if (uiState.carregando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = verde
                )
            }
            return@Scaffold
        }

        val benfeitoriaIdAtual = uiState.benfeitoriaId

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (uiState.editando) {
                    "Atualize os dados da benfeitoria."
                } else {
                    "Informe os dados da nova benfeitoria."
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            uiState.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    color = Color(0xFFD32F2F),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Categoria",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = uiState.categoriaSelecionada == CategoriaBenfeitoria.REPRODUTIVA,
                    onClick = {
                        viewModel.alterarCategoria(CategoriaBenfeitoria.REPRODUTIVA)
                    },
                    label = {
                        Text("Reprodutiva")
                    }
                )

                FilterChip(
                    selected = uiState.categoriaSelecionada == CategoriaBenfeitoria.NAO_REPRODUTIVA,
                    onClick = {
                        viewModel.alterarCategoria(CategoriaBenfeitoria.NAO_REPRODUTIVA)
                    },
                    label = {
                        Text("Não reprodutiva")
                    }
                )
            }

            OutlinedTextField(
                value = uiState.nome,
                onValueChange = viewModel::alterarNome,
                label = {
                    Text("Nome da benfeitoria *")
                },
                placeholder = {
                    Text("Ex.: Curral, pastagem, cerca, galpão")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = viewModel::alterarDescricao,
                label = {
                    Text("Descrição")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = uiState.quantidade,
                    onValueChange = viewModel::alterarQuantidade,
                    label = {
                        Text("Quantidade")
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.unidade,
                    onValueChange = viewModel::alterarUnidade,
                    label = {
                        Text("Unidade")
                    },
                    placeholder = {
                        Text("ha, m, un")
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = uiState.estadoConservacao,
                onValueChange = viewModel::alterarEstadoConservacao,
                label = {
                    Text("Estado de conservação")
                },
                placeholder = {
                    Text("Bom, regular, ruim")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.idadeAproximada,
                onValueChange = viewModel::alterarIdadeAproximada,
                label = {
                    Text("Idade aproximada")
                },
                placeholder = {
                    Text("Ex.: 5 anos")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = viewModel::alterarObservacao,
                label = {
                    Text("Observação")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (uiState.editando && benfeitoriaIdAtual != null && onFotosClick != null) {
                OutlinedButton(
                    onClick = {
                        onFotosClick(benfeitoriaIdAtual)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Abrir fotos da benfeitoria")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    viewModel.salvarBenfeitoria(
                        onSuccess = onSaveSuccess
                    )
                },
                enabled = !uiState.salvando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Text(
                    text = when {
                        uiState.salvando -> "Salvando..."
                        uiState.editando -> "Salvar alterações"
                        else -> "Salvar benfeitoria"
                    }
                )
            }
        }
    }
}