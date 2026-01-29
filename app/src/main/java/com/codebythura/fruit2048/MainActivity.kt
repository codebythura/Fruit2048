package com.codebythura.fruit2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codebythura.fruit2048.ui.home.HomeScreen
import com.codebythura.fruit2048.ui.theme.Fruit2048Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fruit2048Theme {
                Fruit2048()
            }
        }
    }
}
