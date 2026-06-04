package br.com.agrobox.ruralcoleta.ui.variavel.opcoes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import br.com.agrobox.ruralcoleta.ui.components.ConfirmDeleteDialog

@Composable
fun OpcoesVariavelScreen(
    viewModel: OpcoesVariavelViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val opcaoParaExcluir = remember {
        mutableStateOf<OpcaoVariavelEntity?>(null)
    }

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    opcaoParaExcluir.value?.let { opcao ->
        ConfirmDeleteDialog(
            title = "Excluir opção",
            message = "Deseja realmente excluir esta opção?",
            onConfirm = {
                viewModel.excluirOpcao(opcao)
                opcaoParaExcluir.value = null
            },
            onDismiss = {
                opcaoParaExcluir.value = null
            }
        )
    }

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
                            text = "Opções da variável",
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = viewModel::alterarDescricao,
                label = {
                    Text("Descrição da opção")
                },
                placeholder = {
                    Text("Ex.: Asfalto")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = uiState.ordem,
                onValueChange = viewModel::alterarOrdem,
                label = {
                    Text("Ordem")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.salvarOpcao()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlaylistAdd,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Adicionar opção")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.opcoes) { opcao ->
                    OpcaoItem(
                        opcao = opcao,
                        onDeleteClick = {
                            opcaoParaExcluir.value = opcao
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OpcaoItem(
    opcao: OpcaoVariavelEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = opcao.descricao,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ordem: ${opcao.ordem}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = Color(0xFFD32F2F)
                )
            }
        }
    }
}