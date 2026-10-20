package com.example.claudbarcom.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    var mascotState by remember { mutableStateOf(MascotState.IDLE) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0a0a15),
                        Color(0xFF05060f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderClaudbar()
            Spacer(modifier = Modifier.height(40.dp))
            GlassChip(text = "📍 26° · Kissimmee · Nublado")
            Spacer(modifier = Modifier.height(40.dp))

            Box(modifier = Modifier.size(120.dp)) {
                CloudMascot(
                    state = mascotState,
                    size = 120f,
                    onStateChange = { mascotState = it }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "¿Qué tarea hago por ti, ANGEL?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF0F0F8),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "¡Esai ¿Cómo estás?",
                fontSize = 14.sp,
                color = Color(0xFF6c6e82),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            ControlButtons(onMascotStateChange = { mascotState = it })
            Spacer(modifier = Modifier.height(40.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            GlassInput()
            Spacer(modifier = Modifier.height(16.dp))
            BottomActions()
        }
    }
}

@Composable
fun HeaderClaudbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlassChip(text = "📱 Pantalla completa")
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFA78BFA), Color(0xFFF26B21))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", fontSize = 18.sp)
        }
    }
}

@Composable
fun GlassChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFFFFF).copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFA78BFA).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, fontSize = 12.sp, color = Color(0xFF9a9db3))
    }
}

@Composable
fun ControlButtons(onMascotStateChange: (MascotState) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth(0.9f), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GlassButton("Auto", Modifier.weight(1f), onClick = { onMascotStateChange(MascotState.SPEAKING) })
            GlassButton("🤖 Agente", Modifier.weight(1f), onClick = { onMascotStateChange(MascotState.THINKING) })
            GlassButton("🕐", Modifier.weight(1f), onClick = { onMascotStateChange(MascotState.IDLE) })
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GlassButton("✏️", Modifier.weight(1f), onClick = { onMascotStateChange(MascotState.EXCITED) })
            GlassButton("🎙️ Bar Live", Modifier.weight(2f), isPrimary = true, onClick = { onMascotStateChange(MascotState.LISTENING) })
        }
    }
}

@Composable
fun GlassButton(text: String, modifier: Modifier = Modifier, isPrimary: Boolean = false, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) Color(0xFFA78BFA) else Color(0xFFFFFFFF).copy(alpha = 0.08f)
        ),
        border = if (!isPrimary) BorderStroke(1.dp, Color(0xFFFFFFFF).copy(alpha = 0.1f)) else null,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text, fontSize = 12.sp, color = if (isPrimary) Color.White else Color(0xFFF0F0F8), fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun GlassInput() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFFFFFFF).copy(alpha = 0.08f), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFA78BFA).copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("+", fontSize = 16.sp, color = Color(0xFFA78BFA))
        TextField(value = "", onValueChange = {}, modifier = Modifier.weight(1f), placeholder = { Text("Pregunta cualquier cosa...") })
        Text("✨", fontSize = 14.sp)
        Text("🎙️", fontSize = 14.sp)
        Text("↑", fontSize = 14.sp, color = Color(0xFFA78BFA))
    }
}

@Composable
fun BottomActions() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        listOf("🔄", "❤️", "⭕", "📋").forEach { icon ->
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFFFFF).copy(alpha = 0.08f), CircleShape)
                    .border(1.dp, Color(0xFFFFFFFF).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 14.sp)
            }
        }
    }
}
