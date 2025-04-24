package com.upaep.interpretai

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*

@Composable
fun InterpretarScreen(navController: NavController) {
    val context = LocalContext.current
    val interpreter = remember { LSMInterpreter(context) }

    var resultado by remember { mutableStateOf("Esperando seña...") }

    // Mapeo de etiquetas crudas a letras reales
    val etiquetas = mapOf(
        "1" to "A",
        "2" to "B",
        "3" to "C",
        "4" to "D",
        "5" to "E",
        "6" to "F",
        "7" to "G",
        "8" to "H",
        "9" to "I",
        "10" to "L",
        "11" to "M"
    )

    // Configura TextToSpeech
    val tts = remember {
        TextToSpeech(context, null).apply {
            language = Locale("es", "MX")
        }
    }

    // Llama TTS cuando cambia resultado
    LaunchedEffect(resultado) {
        if (resultado != "Esperando seña..." && resultado != "No detectado") {
            tts.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Interpretar",
            fontSize = 28.sp,
            color = Color(0xFF002F5F),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CameraInput(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) { vector ->
            vector?.let {
                val prediccion = interpreter.predict(it)  // Por ejemplo, "4"
                val letra = etiquetas[prediccion] ?: "¿?"
                if (letra != resultado) {
                    resultado = letra
                }
            } ?: run {
                resultado = "No detectado"
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Resultado: $resultado",
            fontSize = 18.sp,
            color = Color(0xFF002F5F)
        )

        Spacer(modifier = Modifier.height(24.dp))

        IconButton(
            onClick = {
                tts.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null)
            },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF002F5F))
        ) {
            Icon(
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = "Audio",
                tint = Color.White
            )
        }
    }
}
