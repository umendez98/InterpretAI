package com.upaep.interpretai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.upaep.interpretai.ui.theme.InterpretAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterpretAITheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "inicio") {
        composable("inicio") { InicioScreen(navController) }
        composable("instrucciones") { InstruccionesScreen(navController) }
        composable("interpretar") { InterpretarScreen(navController) }
        composable("ajustes") { AjustesScreen(navController) }
    }
}
