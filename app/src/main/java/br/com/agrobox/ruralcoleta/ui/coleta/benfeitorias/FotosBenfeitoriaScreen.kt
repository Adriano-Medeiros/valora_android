package br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar
import br.com.agrobox.ruralcoleta.ui.components.ConfirmDeleteDialog
import br.com.agrobox.ruralcoleta.util.CameraHelper
import br.com.agrobox.ruralcoleta.util.FileHelper
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun FotosBenfeitoriaScreen(
    viewModel: FotosBenfeitoriaViewModel,
    benfeitoriaId: Long,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val arquivoAtual = remember {
        mutableStateOf<File?>(null)
    }

    val fotoParaExcluir = remember {
        mutableStateOf<FotoBenfeitoriaEntity?>(null)
    }

    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    lateinit var cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>

    cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { sucesso ->

        if (sucesso) {
            arquivoAtual.value?.let { arquivo ->
                viewModel.salvarFoto(
                    caminho = arquivo.absolutePath
                )
            }
        } else {
            arquivoAtual.value?.delete()

            Toast.makeText(
                context,
                "Foto não capturada.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val permissaoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permitido ->

        if (permitido) {
            val arquivo = FileHelper.criarArquivoImagemBenfeitoria(
                context = context,
                benfeitoriaId = benfeitoriaId
            )

            arquivoAtual.value = arquivo

            val uri = CameraHelper.criarUriImagem(
                context = context,
                file = arquivo
            )

            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(
                context,
                "Permissão da câmera negada.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun abrirCamera() {
        permissaoCameraLauncher.launch(
            Manifest.permission.CAMERA
        )
    }

    fotoParaExcluir.value?.let { foto ->
        ConfirmDeleteDialog(
            title = "Excluir foto",
            message = "Deseja realmente excluir esta foto da benfeitoria?",
            onConfirm = {
                viewModel.excluirFoto(foto)
                fotoParaExcluir.value = null
            },
            onDismiss = {
                fotoParaExcluir.value = null
            }
        )
    }

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Fotos da benfeitoria",
                stepText = "5 de 8",
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
                text = "Registrar fotos da benfeitoria",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Adicione fotos específicas desta benfeitoria.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = uiState.legenda,
                onValueChange = viewModel::alterarLegenda,
                label = {
                    Text("Legenda da foto")
                },
                placeholder = {
                    Text("Ex.: Vista lateral do curral")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    abrirCamera()
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
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Tirar foto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.fotos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.height(42.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nenhuma foto adicionada",
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.fotos) { foto ->
                        FotoBenfeitoriaGridItem(
                            foto = foto,
                            onDeleteClick = {
                                fotoParaExcluir.value = foto
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FotoBenfeitoriaGridItem(
    foto: FotoBenfeitoriaEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = File(foto.caminhoArquivo)
                ),
                contentDescription = foto.legenda,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir foto",
                    tint = Color.White
                )
            }
        }

        Text(
            text = foto.legenda ?: "Sem legenda",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}