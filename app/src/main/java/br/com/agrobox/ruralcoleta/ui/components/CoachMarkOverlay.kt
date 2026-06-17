package br.com.agrobox.ruralcoleta.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun CoachMarkOverlay(
    targetBounds: Rect?,
    passoTexto: String,
    titulo: String,
    descricao: String,
    textoBotaoPrimario: String,
    onBotaoPrimarioClick: () -> Unit,
    textoBotaoSecundario: String? = null,
    onBotaoSecundarioClick: (() -> Unit)? = null,
    onPularClick: () -> Unit
) {
    if (targetBounds == null) {
        return
    }

    val verde = Color(0xFF00823B)
    val verdeEscuro = Color(0xFF003B24)
    val density = LocalDensity.current

    val infiniteTransition = rememberInfiniteTransition(
        label = "tutorial_primeiro_acesso"
    )

    val deslocamentoMao = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650),
            repeatMode = RepeatMode.Reverse
        ),
        label = "movimento_mao"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1000f)
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) {
                // Bloqueia cliques acidentais no conteúdo atrás do tutorial.
            }
    ) {
        val mostrarCardNoTopo = targetBounds.center.y > constraints.maxHeight / 2f

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
        ) {
            drawRect(
                color = Color.Black.copy(alpha = 0.74f)
            )

            val margem = 10.dp.toPx()

            val left = (targetBounds.left - margem).coerceAtLeast(0f)
            val top = (targetBounds.top - margem).coerceAtLeast(0f)
            val right = (targetBounds.right + margem).coerceAtMost(size.width)
            val bottom = (targetBounds.bottom + margem).coerceAtMost(size.height)

            drawRoundRect(
                color = Color.Transparent,
                topLeft = Offset(left, top),
                size = Size(
                    width = right - left,
                    height = bottom - top
                ),
                cornerRadius = CornerRadius(
                    x = 20.dp.toPx(),
                    y = 20.dp.toPx()
                ),
                blendMode = BlendMode.Clear
            )

            drawRoundRect(
                color = Color.White.copy(alpha = 0.95f),
                topLeft = Offset(left, top),
                size = Size(
                    width = right - left,
                    height = bottom - top
                ),
                cornerRadius = CornerRadius(
                    x = 20.dp.toPx(),
                    y = 20.dp.toPx()
                ),
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )
        }

        Text(
            text = "👆",
            fontSize = 34.sp,
            modifier = Modifier.offset(
                x = with(density) {
                    (targetBounds.center.x - 18f).toDp()
                },
                y = with(density) {
                    (targetBounds.bottom - 22f + deslocamentoMao.value).toDp()
                }
            )
        )

        Card(
            modifier = Modifier
                .align(
                    if (mostrarCardNoTopo) {
                        Alignment.TopCenter
                    } else {
                        Alignment.BottomCenter
                    }
                )
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = passoTexto,
                    color = verde,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    text = titulo,
                    color = verdeEscuro,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = descricao,
                    color = Color(0xFF555555),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onPularClick
                    ) {
                        Text(
                            text = "Pular",
                            color = Color.Gray
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (
                            textoBotaoSecundario != null &&
                            onBotaoSecundarioClick != null
                        ) {
                            OutlinedButton(
                                onClick = onBotaoSecundarioClick,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(textoBotaoSecundario)
                            }
                        }

                        Button(
                            onClick = onBotaoPrimarioClick,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(textoBotaoPrimario)
                        }
                    }
                }
            }
        }
    }
}