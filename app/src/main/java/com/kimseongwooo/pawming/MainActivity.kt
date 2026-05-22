package com.kimseongwooo.pawming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme
import com.kimseongwooo.pawming.navigation.PawmingNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawmingTheme {
                PawmingNavGraph()
            }
        }
    }
}
