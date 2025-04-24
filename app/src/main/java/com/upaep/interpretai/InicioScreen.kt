package com.upaep.interpretai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InicioScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Un mundo\na una\n",
                fontSize = 26.sp,
                color = Color(0xFF002F5F)
            )
            Text(
                text = "seña",
                fontSize = 26.sp,
                color = Color(0xFF002F5F),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "de distancia",
                fontSize = 26.sp,
                color = Color(0xFF002F5F)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController.navigate("instrucciones") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002F5F))
            ) {
                Text(text = "Empezar →", color = Color.White)
            }
        }
    }
}
