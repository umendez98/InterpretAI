package com.upaep.interpretai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InstruccionesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Acepta los permisos de uso de cámara\n\n" +
                        "Realiza señas LSM frente a la cámara\n\n" +
                        "Leé la interpretación en tiempo real\n\n" +
                        "Activa o desactiva el lector en audio\n\n" +
                        "¡Eso es todo!, disfruta de un mundo sin fronteras",
                fontSize = 18.sp,
                color = Color(0xFF002F5F)
            )
        }

        Button(
            onClick = { navController.navigate("interpretar") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002F5F))
        ) {
            Text(text = "¡Entendido! →", color = Color.White)
        }
    }
}
