package br.com.agrobox.ruralcoleta.ui.coleta

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.R
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import br.com.agrobox.ruralcoleta.ui.components.ColetaTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaColetaScreen(
    viewModel: NovaColetaViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit

) {
    val uiState by viewModel.uiState.collectAsState()

    val verdeEscuro = Color(0xFF003B24)
    val verde = Color(0xFF00823B)
    val fundo = Color(0xFFF7F8F7)

    Scaffold(
        containerColor = fundo,
        topBar = {
            ColetaTopBar(
                title = "Nova coleta",
                stepText = "1 de 8",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Selecione o tipo de coleta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F1F1F)
            )

            TipoColetaCard(
                titulo = "Imóvel Avaliando",
                descricao = "Imóvel principal que será objeto da avaliação.",
                imageRes = R.drawable.ic_imovel_avaliando,
                selected = uiState.tipoColeta == TipoColeta.AVALIANDO,
                selectedColor = verde,
                onClick = {
                    viewModel.alterarTipo(TipoColeta.AVALIANDO)
                }
            )

            TipoColetaCard(
                titulo = "Dado Amostral",
                descricao = "Imóveis comparativos de mercado.",
                imageRes = R.drawable.ic_dado_amostral,
                selected = uiState.tipoColeta == TipoColeta.AMOSTRAL,
                selectedColor = Color(0xFF2E9BB5),
                onClick = {
                    viewModel.alterarTipo(TipoColeta.AMOSTRAL)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // depois navegaremos para Dados Gerais
                    onNextClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Text("Próximo")
            }
        }
    }
}

@Composable
private fun TipoColetaCard(
    titulo: String,
    descricao: String,
    imageRes: Int,
    selected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = if (selected) {
            ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.5.dp,
                brush = androidx.compose.ui.graphics.SolidColor(selectedColor)
            )
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = titulo,
                modifier = Modifier.size(58.dp)
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F1F1F)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
            }

            if (selected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selecionado",
                    tint = selectedColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}