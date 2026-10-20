package com.example.claudbarcom.ui.home

import androidx.compose.animation.animateAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

enum class MascotState {
    IDLE, THINKING, LISTENING, SPEAKING, SUCCESS, ERROR, SLEEPING, EXCITED, PHONE
}

class MascotMachine {
    var state = MascotState.IDLE
        private set
    var onStateChange: (MascotState) -> Unit = {}

    fun setState(newState: MascotState) {
        if (newState != state) {
            state = newState
            onStateChange(state)
        }
    }

    fun touch() {
        if (state == MascotState.SLEEPING) setState(MascotState.IDLE)
    }
}

@Composable
fun CloudMascot(
    modifier: Modifier = Modifier,
    state: MascotState = MascotState.IDLE,
    size: Float = 120f,
    onStateChange: (MascotState) -> Unit = {}
) {
    val machine = remember { MascotMachine().apply { onStateChange = onStateChange } }

    LaunchedEffect(state) {
        machine.setState(state)
    }

    val floatAnimation = rememberInfiniteTransition(label = "float")
    val floatY by floatAnimation.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val breatheAnimation = rememberInfiniteTransition(label = "breathe")
    val breatheScale by breatheAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val pupilScaleAnim by animateAsState(
        targetValue = when (machine.state) {
            MascotState.LISTENING -> 1.14f
            MascotState.EXCITED -> 1.18f
            else -> 1f
        },
        animationSpec = tween(300),
        label = "pupilScale"
    )

    val mouthPathAnim by animateAsState(
        targetValue = when (machine.state) {
            MascotState.LISTENING -> "listening"
            MascotState.THINKING -> "thinking"
            MascotState.SPEAKING -> "speaking"
            MascotState.SUCCESS -> "success"
            MascotState.ERROR -> "error"
            MascotState.SLEEPING -> "sleeping"
            MascotState.EXCITED -> "excited"
            MascotState.PHONE -> "phone"
            else -> "idle"
        },
        animationSpec = tween(300),
        label = "mouthPath"
    )

    Box(
        modifier = modifier
            .size(size.dp)
            .graphicsLayer(
                translationY = floatY,
                scaleX = breatheScale,
                scaleY = breatheScale
            )
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    machine.touch()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Cloud,
            contentDescription = "Cloud Mascot: ${machine.state}",
            modifier = Modifier
                .size(size.dp * 0.8f)
                .alpha(0.9f),
            tint = Color(0xA78BFA)
        )

        if (machine.state == MascotState.LISTENING) {
            val rippleAnimation = rememberInfiniteTransition(label = "ripple")
            val rippleScale by rippleAnimation.animateFloat(
                initialValue = 0.9f,
                targetValue = 1.12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1800, easing = EaseOut),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ripple"
            )
            val rippleAlpha by rippleAnimation.animateFloat(
                initialValue = 0.8f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1800, easing = EaseOut),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rippleAlpha"
            )

            Box(
                modifier = Modifier
                    .size(size.dp)
                    .scale(rippleScale)
                    .alpha(rippleAlpha)
                    .background(
                        color = Color(0x7DD3FC),
                        shape = CircleShape
                    )
            )
        }

        if (machine.state == MascotState.THINKING) {
            val thinkAnimation = rememberInfiniteTransition(label = "think")
            val dot1Y by thinkAnimation.animateFloat(
                initialValue = 0f,
                targetValue = -4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1300, easing = EaseInOutCubic, delayMillis = 0),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot1"
            )
        }
    }
}

@Composable
fun CloudMascotWithSidebar(
    state: MascotState = MascotState.IDLE,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(70.dp)
            .background(
                color = Color(0xA78BFA).copy(alpha = 0.08f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        CloudMascot(
            state = state,
            size = 50f,
            modifier = Modifier
        )
    }
}
