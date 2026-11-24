package com.codebythura.fruit2048.ui.game.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.codebythura.fruit2048.R

private val _lightColors = listOf(
    Color(0xFF08B928),
    Color(0xFFBDB00E),
    Color(0xFFE86916),
    Color(0xFF6E14EA)
)

private val _darkColors = listOf(
    Color(0xFF12C52E),
    Color(0xFFDACC39),
    Color(0xFFE17B37),
    Color(0xFF6D28D5)
)

@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    val colors = if(isSystemInDarkTheme()) _darkColors else _lightColors
    Column(modifier = modifier) {
        Text(
            text = "2048",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(colors),
            ),
            modifier = Modifier.testTag(stringResource(R.string.title_2048))
        )
        Text(
            text = "Fruit Edition!",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}