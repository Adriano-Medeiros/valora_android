package br.com.agrobox.ruralcoleta.ui.coleta

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar
import br.com.agrobox.ruralcoleta.util.LocationHelper
import kotlinx.coroutines.launch

@Composable
fun DadosGeraisColetaScreen(
    viewModel: NovaColetaViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val granted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {

            coroutineScope.launch {

                val coordenadas =
                    LocationHelper.capturarLocalizacaoAtual(context)

                if (coordenadas != null) {

                    viewModel.alterarLatitude(
                        coordenadas.first
                    )

                    viewModel.alterarLongitude(
                        coordenadas.second
                    )

                } else {

                    Toast.makeText(
                        context,
                        "Não foi possível obter a localização atual. Ative o GPS e tente novamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        } else {

            Toast.makeText(
                context,
                "Permissão de localização negada.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun capturarCoordenadas() {

        if (LocationHelper.temPermissaoLocalizacao(context)) {

            coroutineScope.launch {

                val coordenadas =
                    LocationHelper.capturarLocalizacaoAtual(context)

                if (coordenadas != null) {

                    viewModel.alterarLatitude(
                        coordenadas.first
                    )

                    viewModel.alterarLongitude(
                        coordenadas.second
                    )

                } else {

                    Toast.makeText(
                        context,
                        "Não foi possível obter a localização atual. Ative o GPS e tente novamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val verde = Color(0xFF00823B)

    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,

        topBar = {

            ColetaTopBar(
                title = "Dados gerais",
                stepText = "3 de 8",
                onBackClick = onBackClick
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = "Informações básicas",

                style = MaterialTheme.typography.titleSmall,

                fontWeight = FontWeight.Bold,

                color = Color(0xFF003B24)
            )

            OutlinedTextField(
                value = uiState.nomeReferencia,

                onValueChange = viewModel::alterarNome,

                label = {
                    Text("Nome / Referência *")
                },

                isError = uiState.erroNomeReferencia,

                supportingText = {

                    if (uiState.erroNomeReferencia) {

                        Text("Campo obrigatório")
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = uiState.municipio,

                onValueChange = viewModel::alterarMunicipio,

                label = {
                    Text("Município *")
                },

                isError = uiState.erroMunicipio,

                supportingText = {

                    if (uiState.erroMunicipio) {

                        Text("Campo obrigatório")
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = uiState.uf,

                onValueChange = viewModel::alterarUf,

                label = {
                    Text("UF *")
                },

                isError = uiState.erroUf,

                supportingText = {

                    if (uiState.erroUf) {

                        Text("Campo obrigatório")
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )
            OutlinedTextField(
                value = uiState.informante,

                onValueChange = viewModel::alterarInformante,

                label = {
                    Text("Informante *")
                },

                isError = uiState.erroInformante,

                supportingText = {

                    if (uiState.erroInformante) {

                        Text("Campo obrigatório")
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = uiState.contatoInformante,

                onValueChange = viewModel::alterarContatoInformante,

                label = {
                    Text("Contato do informante *")
                },

                isError = uiState.erroContatoInformante,

                supportingText = {

                    if (uiState.erroContatoInformante) {

                        Text("Campo obrigatório")
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true
            )

            Text(
                text = "Coordenadas",

                style = MaterialTheme.typography.titleSmall,

                fontWeight = FontWeight.Bold,

                color = Color(0xFF003B24)
            )

            OutlinedTextField(
                value = uiState.latitude,

                onValueChange = viewModel::alterarLatitude,

                label = {
                    Text("Latitude")
                },

                modifier = Modifier.fillMaxWidth(),

                trailingIcon = {

                    IconButton(
                        onClick = {
                            capturarCoordenadas()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Capturar latitude"
                        )
                    }
                },

                singleLine = true
            )

            OutlinedTextField(
                value = uiState.longitude,

                onValueChange = viewModel::alterarLongitude,

                label = {
                    Text("Longitude")
                },

                modifier = Modifier.fillMaxWidth(),

                trailingIcon = {

                    IconButton(
                        onClick = {
                            capturarCoordenadas()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Capturar longitude"
                        )
                    }
                },

                singleLine = true
            )

            Button(
                onClick = {
                    capturarCoordenadas()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {

                Text("Capturar coordenadas")
            }

            uiState.mensagemErro?.let { mensagem ->

                Text(
                    text = mensagem,

                    color = Color(0xFFD32F2F),

                    style = MaterialTheme.typography.bodySmall,

                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = onNextClick,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {

                Text("Salvar e continuar")
            }
        }
    }
}