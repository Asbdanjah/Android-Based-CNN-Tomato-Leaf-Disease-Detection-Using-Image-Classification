package com.asbdanja.tomatoguard.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asbdanja.tomatoguard.theme.* // Imports your updated global theme colors and White38
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val loaderProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // 1. Extract the themed color here, while we are in the Composable context
    val borderColor = MaterialTheme.colorScheme.primaryContainer
    val progressBarColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    LaunchedEffect(Unit) {
        scope.launch {
            loaderProgress.animateTo(
                targetValue  = 1f,
                animationSpec = tween(durationMillis = 1400)
            )
        }
        delay(2000L)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ForestGreen),
        contentAlignment = Alignment.Center
    ) {
        RingDecoration()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .drawBehind {
                        drawRoundRect(
                            color        = borderColor, // Use the extracted variable
                            cornerRadius = CornerRadius(24.dp.toPx()),
                            style        = Stroke(width = 1.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                TomatoLogoMark()
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text          = "TomatoGuard",
                color         = Color.White,
                fontSize      = 22.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = (-0.3).sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text          = "DISEASE DETECTION",
                color         = White38,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Normal,
                letterSpacing = 1.2.sp
            )

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.10f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(loaderProgress.value)
                        .clip(RoundedCornerShape(2.dp))
                        .background(progressBarColor) // Use the extracted variable
                )
            }
        }

        Text(
            text     = "v1.0.0",
            color    = Color.White.copy(alpha = 0.20f),
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}



@Composable
private fun RingDecoration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height * 0.28f
        listOf(380f, 280f, 180f).forEach { radius ->
            drawCircle(
                color       = Color.White.copy(alpha = 0.025f),
                radius      = radius,
                center      = Offset(cx, cy),
                style       = Stroke(width = 1f)
            )
        }
    }
}

@Composable
private fun TomatoLogoMark() {
    val shieldColor = MaterialTheme.colorScheme.surface
    Canvas(modifier = Modifier.size(48.dp)) {
        val w = size.width
        val h = size.height

        // Tomato body
        drawOval(
            color = TomatoRed,
            topLeft = Offset(w * 0.14f, h * 0.42f),
            size = Size(w * 0.72f, h * 0.52f)
        )

        // Highlight
        drawOval(
            color = Color(0xFFEF8080).copy(alpha = 0.35f),
            topLeft = Offset(w * 0.22f, h * 0.32f),
            size = Size(w * 0.22f, h * 0.16f)
        )

        // Calyx
        drawPath(
            path = Path().apply {
                moveTo(w * 0.35f, h * 0.36f)
                quadraticBezierTo(w * 0.5f, h * 0.28f, w * 0.65f, h * 0.36f)
                quadraticBezierTo(w * 0.56f, h * 0.40f, w * 0.5f, h * 0.38f)
                quadraticBezierTo(w * 0.44f, h * 0.40f, w * 0.35f, h * 0.36f)
                close()
            },
            color = TomatoRedDark
        )

        // Stem
        drawLine(
            color = LeafGreen,
            start = Offset(w * 0.5f, h * 0.36f),
            end = Offset(w * 0.44f, h * 0.15f),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )

        // Leaves
        drawPath(
            path = Path().apply {
                moveTo(w * 0.44f, h * 0.15f)
                quadraticBezierTo(w * 0.22f, h * 0.02f, w * 0.16f, h * 0.12f)
                quadraticBezierTo(w * 0.24f, h * 0.24f, w * 0.44f, h * 0.23f)
                close()
            },
            color = LeafGreen
        )

        drawPath(
            path = Path().apply {
                moveTo(w * 0.44f, h * 0.15f)
                quadraticBezierTo(w * 0.58f, h * 0.02f, w * 0.70f, h * 0.08f)
                quadraticBezierTo(w * 0.64f, h * 0.22f, w * 0.46f, h * 0.23f)
                close()
            },
            color = LeafGreenLight
        )

        // Shield badge
        val sx = w * 0.64f
        val sy = h * 0.60f
        drawPath(
            path = Path().apply {
                moveTo(sx, sy - h * 0.14f)
                lineTo(sx + w * 0.11f, sy - h * 0.08f)
                lineTo(sx + w * 0.11f, sy + h * 0.04f)
                quadraticBezierTo(sx + w * 0.11f, sy + h * 0.14f, sx, sy + h * 0.17f)
                quadraticBezierTo(sx - w * 0.11f, sy + h * 0.14f, sx - w * 0.11f, sy + h * 0.04f)
                lineTo(sx - w * 0.11f, sy - h * 0.08f)
                close()
            },
            color = shieldColor
        )

        // Checkmark inside shield
        drawLine(
            LeafGreenLight,
            Offset(sx - w * 0.05f, sy + h * 0.02f),
            Offset(sx - w * 0.01f, sy + h * 0.07f),
            2f,
            cap = StrokeCap.Round
        )
        drawLine(
            LeafGreenLight,
            Offset(sx - w * 0.01f, sy + h * 0.07f),
            Offset(sx + w * 0.06f, sy - h * 0.03f),
            2f,
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    TomatoGuardTheme(darkTheme = true) {
        SplashScreen(onNavigateToHome = {})
    }
}
