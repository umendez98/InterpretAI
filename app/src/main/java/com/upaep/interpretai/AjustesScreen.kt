package com.upaep.interpretai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AjustesScreen(navController: NavController) {
    var darkMode by remember { mutableStateOf(false) }
    var setting1 by remember { mutableStateOf(true) }
    var setting2 by remember { mutableStateOf(false) }
    var setting3 by remember { mutableStateOf(false) }
    var setting4 by remember { mutableStateOf(true) }
    var setting5 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Ajustes",
                fontSize = 28.sp,
                color = Color(0xFF002F5F)
            )

            Spacer(modifier = Modifier.height(24.dp))

            RowSetting("Modo oscuro", darkMode) { darkMode = it }
            RowSetting("Algún ajuste", setting1) { setting1 = it }
            RowSetting("Desactivable", setting2) { setting2 = it }
            RowSetting("Otro más", setting3) { setting3 = it }
            RowSetting("Lorem", setting4) { setting4 = it }
            RowSetting("Lorem", setting5) { setting5 = it }
        }

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002F5F)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "← Volver", color = Color.White)
        }
    }
}

@Composable
fun RowSetting(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color(0xFF002F5F))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
