package br.com.agrobox.ruralcoleta.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.agrobox.ruralcoleta.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: () -> Unit
) {

    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {

        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 5000
            )
        )

        delay(300)

        onFinish()
    }

    Box(
        modifier = Modifier.fillMaxSize()
       ) {

        Image(
            painter = painterResource(id = R.drawable.splashcomfolhas),
            contentDescription = "Splash",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        ) {

            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier
                    .width(260.dp)
                    .height(6.dp),
                color = Color(0xFFB7F52A),
                trackColor = Color(0x6600FF99)
            )
        }
    }
}