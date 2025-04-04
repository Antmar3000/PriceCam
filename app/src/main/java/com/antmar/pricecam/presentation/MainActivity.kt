package com.antmar.pricecam.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.antmar.pricecam.presentation.ui.utils.KeepScreenOn
import com.antmar.pricecam.presentation.ui.theme.PriceCamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PriceCamTheme {

                KeepScreenOn()
                MainScreen()
            }
        }
    }
}

