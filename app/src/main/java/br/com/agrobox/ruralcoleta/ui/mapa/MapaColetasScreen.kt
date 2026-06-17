package br.com.agrobox.ruralcoleta.ui.mapa

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.StatusColeta
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaColetasScreen(
    viewModel: MapaColetasViewModel,
    onAbrirColetaClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val coletaSelecionada = remember {
        mutableStateOf<ColetaEntity?>(null)
    }

    val mapViewState = remember {
        mutableStateOf<MapView?>(null)
    }

    val verdeEscuro = Color(0xFF003B24)
    val fundo = Color(0xFFF7F8F7)

    val coletasFiltradas = uiState.coletasFiltradas
    val mapaJaCentralizado = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(coletasFiltradas.size) {
        mapaJaCentralizado.value = false
    }
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_RESUME -> {
                    mapViewState.value?.onResume()
                }

                androidx.lifecycle.Lifecycle.Event.ON_PAUSE -> {
                    mapViewState.value?.onPause()
                }

                androidx.lifecycle.Lifecycle.Event.ON_DESTROY -> {
                    mapViewState.value?.onDetach()
                    mapViewState.value = null
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    coletaSelecionada.value?.let { coleta ->
        ModalBottomSheet(
            onDismissRequest = {
                coletaSelecionada.value = null
            },
            sheetState = rememberModalBottomSheetState()
        ) {
            ColetaMapaBottomSheet(
                coleta = coleta,
                onAbrirClick = {
                    coletaSelecionada.value = null

                    onAbrirColetaClick(coleta.id)
                }
            )
        }
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
                    Spacer(modifier = Modifier.width(48.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mapa das coletas",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    IconButton(
                        onClick = {
                            mapViewState.value?.post {
                                centralizarMapa(
                                    mapView = mapViewState.value,
                                    coletas = coletasFiltradas
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Centralizar",
                            tint = Color.White
                        )
                    }
                }

                FiltrosMapa(
                    mostrarAvaliando = uiState.mostrarAvaliando,
                    mostrarAmostral = uiState.mostrarAmostral,
                    mostrarRascunho = uiState.mostrarRascunho,
                    mostrarConcluida = uiState.mostrarConcluida,
                    onAvaliandoClick = viewModel::alternarAvaliando,
                    onAmostralClick = viewModel::alternarAmostral,
                    onRascunhoClick = viewModel::alternarRascunho,
                    onConcluidaClick = viewModel::alternarConcluida
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mapViewState.value?.post {
                        centralizarMapa(
                            mapView = mapViewState.value,
                            coletas = coletasFiltradas
                        )
                    }
                },
                containerColor = Color(0xFF00823B)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Centralizar",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(7.0)
                        controller.setCenter(
                            GeoPoint(-9.665, -36.650)
                        )
                        mapViewState.value = this
                    }
                },
                update = { mapView ->
                    atualizarMarcadores(
                        context = context,
                        mapView = mapView,
                        coletas = coletasFiltradas,
                        onMarkerClick = { coleta ->
                            coletaSelecionada.value = coleta
                        }
                    )

                    if (!mapaJaCentralizado.value && coletasFiltradas.isNotEmpty()) {
                        mapaJaCentralizado.value = true

                        mapView.post {
                            centralizarMapa(
                                mapView = mapView,
                                coletas = coletasFiltradas
                            )
                        }
                    }
                }
            )

            if (coletasFiltradas.isEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nenhuma coleta com coordenadas",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Preencha latitude e longitude nas coletas para visualizá-las no mapa.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltrosMapa(
    mostrarAvaliando: Boolean,
    mostrarAmostral: Boolean,
    mostrarRascunho: Boolean,
    mostrarConcluida: Boolean,
    onAvaliandoClick: () -> Unit,
    onAmostralClick: () -> Unit,
    onRascunhoClick: () -> Unit,
    onConcluidaClick: () -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FiltroChipMapa(
            texto = "Avaliando",
            selected = mostrarAvaliando,
            onClick = onAvaliandoClick
        )

        FiltroChipMapa(
            texto = "Amostral",
            selected = mostrarAmostral,
            onClick = onAmostralClick
        )

        FiltroChipMapa(
            texto = "Rascunho",
            selected = mostrarRascunho,
            onClick = onRascunhoClick
        )

        FiltroChipMapa(
            texto = "Concluída",
            selected = mostrarConcluida,
            onClick = onConcluidaClick
        )
    }
}

@Composable
private fun FiltroChipMapa(
    texto: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(texto)
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF00823B),
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = Color.Black
        )
    )
}

@Composable
private fun ColetaMapaBottomSheet(
    coleta: ColetaEntity,
    onAbrirClick: () -> Unit
) {
    val verde = Color(0xFF087B35)
    val laranja = Color(0xFFD65F00)
    val azul = Color(0xFF1976D2)
    val textoCinza = Color(0xFF666666)

    val tipoTexto = when (coleta.tipoColeta) {
        TipoColeta.AVALIANDO.name -> "Imóvel avaliando"
        TipoColeta.AMOSTRAL.name -> "Dado amostral"
        else -> coleta.tipoColeta
    }

    val tipoCor = when (coleta.tipoColeta) {
        TipoColeta.AVALIANDO.name -> verde
        TipoColeta.AMOSTRAL.name -> azul
        else -> Color.DarkGray
    }

    val statusTexto = if (coleta.status == StatusColeta.RASCUNHO.name) {
        "Rascunho"
    } else {
        "Concluída"
    }

    val statusCor = if (coleta.status == StatusColeta.RASCUNHO.name) {
        laranja
    } else {
        verde
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {


        Text(
            text = coleta.nomeReferencia,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003B24)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = tipoTexto,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    labelColor = tipoCor
                )
            )

            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = statusTexto,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    labelColor = statusCor
                )
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF7F8F7)
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoMapaLinha(
                    titulo = "Município / UF",
                    valor = montarMunicipioUf(coleta)
                )

                InfoMapaLinha(
                    titulo = "Latitude",
                    valor = coleta.latitude?.toString() ?: "-"
                )

                InfoMapaLinha(
                    titulo = "Longitude",
                    valor = coleta.longitude?.toString() ?: "-"
                )

                InfoMapaLinha(
                    titulo = "Informante",
                    valor = coleta.informante.ifBlank { "-" }
                )

                InfoMapaLinha(
                    titulo = "Contato",
                    valor = coleta.contatoInformante.ifBlank { "-" }
                )

                if (!coleta.observacao.isNullOrBlank()) {
                    InfoMapaLinha(
                        titulo = "Observação",
                        valor = coleta.observacao
                    )
                }
            }
        }

        Text(
            text = "Toque em abrir para continuar, editar ou visualizar os dados dessa coleta.",
            style = MaterialTheme.typography.bodySmall,
            color = textoCinza
        )

        TextButton(
            onClick = onAbrirClick,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF00823B),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Text(
                text = "ABRIR COLETA",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
private fun InfoMapaLinha(
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF666666)
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )
    }
}

private fun montarMunicipioUf(
    coleta: ColetaEntity
): String {
    val municipio = coleta.municipio.ifBlank { "-" }
    val uf = coleta.uf.ifBlank { "-" }

    return "$municipio/$uf"
}
private fun atualizarMarcadores(
    context: Context,
    mapView: MapView,
    coletas: List<ColetaEntity>,
    onMarkerClick: (ColetaEntity) -> Unit
) {
    mapView.overlays.removeAll {
        it is Marker
    }

    coletas.forEach { coleta ->
        val latitude = coleta.latitude ?: return@forEach
        val longitude = coleta.longitude ?: return@forEach

        val marker = Marker(mapView).apply {
            position = GeoPoint(latitude, longitude)

            setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            title = coleta.nomeReferencia

            snippet = buildString {
                append("${coleta.municipio}/${coleta.uf}")
                append(" • ")

                append(
                    if (coleta.status == StatusColeta.RASCUNHO.name) {
                        "Rascunho"
                    } else {
                        "Concluída"
                    }
                )
            }

            setOnMarkerClickListener { _, _ ->
                onMarkerClick(coleta)
                true
            }
        }

        mapView.overlays.add(marker)
    }

    mapView.invalidate()
}
private fun centralizarMapa(
    mapView: MapView?,
    coletas: List<ColetaEntity>
) {
    if (mapView == null || coletas.isEmpty()) {
        return
    }

    val pontos = coletas.mapNotNull { coleta ->
        val lat = coleta.latitude
        val lon = coleta.longitude

        if (lat != null && lon != null) {
            GeoPoint(lat, lon)
        } else {
            null
        }
    }

    if (pontos.isEmpty()) {
        return
    }

    if (mapView.width <= 0 || mapView.height <= 0) {
        mapView.post {
            centralizarMapa(
                mapView = mapView,
                coletas = coletas
            )
        }

        return
    }

    if (pontos.size == 1) {
        mapView.controller.setZoom(14.0)
        mapView.controller.animateTo(pontos.first())
        return
    }

    val norte = pontos.maxOf { it.latitude }
    val sul = pontos.minOf { it.latitude }
    val leste = pontos.maxOf { it.longitude }
    val oeste = pontos.minOf { it.longitude }

    val boundingBox = BoundingBox(
        norte,
        leste,
        sul,
        oeste
    )

    mapView.zoomToBoundingBox(
        boundingBox,
        true,
        80
    )
}