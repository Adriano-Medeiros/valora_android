package br.com.agrobox.ruralcoleta.ui.coleta.editar

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.util.LocationHelper
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import br.com.agrobox.ruralcoleta.util.filtrarDecimal

@Composable
fun EditarDadosGeraisColetaScreen(
    viewModel: EditarDadosGeraisColetaViewModel,
    capturarGpsAutomaticamente: Boolean,
    onBackClick: () -> Unit,
    onSaveSuccess: (Long) -> Unit
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
                val coordenadas = LocationHelper.capturarLocalizacaoAtual(context)

                if (coordenadas != null) {
                    viewModel.alterarLatitude(coordenadas.first)
                    viewModel.alterarLongitude(coordenadas.second)
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
                val coordenadas = LocationHelper.capturarLocalizacaoAtual(context)

                if (coordenadas != null) {
                    viewModel.alterarLatitude(coordenadas.first)
                    viewModel.alterarLongitude(coordenadas.second)
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
    val gpsAutomaticoExecutado = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        capturarGpsAutomaticamente,
        uiState.coletaOriginal
    ) {
        if (
            capturarGpsAutomaticamente &&
            !gpsAutomaticoExecutado.value &&
            uiState.coletaOriginal != null &&
            uiState.latitude.isBlank() &&
            uiState.longitude.isBlank()
        ) {
            gpsAutomaticoExecutado.value = true
            capturarCoordenadas()
        }
    }
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
                            text = "Editar dados gerais",
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = uiState.nomeReferencia,
                onValueChange = viewModel::alterarNome,
                label = { Text("Nome / Referência *") },
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
                label = { Text("Município") },
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
                label = { Text("UF") },
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
                label = { Text("Informante") },
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
                    Text("Contato do informante")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            Text(
                text = "Coordenadas",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003B24)
            )

            OutlinedTextField(
                value = uiState.latitude,
                onValueChange = { valor ->
                    viewModel.alterarLatitude(
                        filtrarDecimal(
                            valor = valor,
                            permitirNegativo = true
                        )
                    )
                },
                label = {
                    Text("Latitude")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
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
                }
            )

            OutlinedTextField(
                value = uiState.longitude,
                onValueChange = { valor ->
                    viewModel.alterarLongitude(
                        filtrarDecimal(
                            valor = valor,
                            permitirNegativo = true
                        )
                    )
                },
                label = {
                    Text("Longitude")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
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
                }
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
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    viewModel.salvar(
                        onSuccess = onSaveSuccess
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
                Text("Salvar e continuar")
            }
        }
    }
}