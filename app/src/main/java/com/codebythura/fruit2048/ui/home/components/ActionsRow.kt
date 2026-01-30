package com.codebythura.fruit2048.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.R


@Composable
fun ActionsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        NewGameButton(modifier = Modifier.weight(1f))
        UndoGameButton(modifier = Modifier)
    }
}

@Composable
fun UndoGameButton(modifier: Modifier = Modifier) {
    Surface(
        onClick = {},
//        color = defaultBoardBgColor,
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.Gray.copy(alpha = 0.4f)),
        content = {
            Image(
                painter = painterResource(R.drawable.undo_light),
                contentDescription = "Undo",
                modifier = Modifier.padding(8.dp)
            )
        }
    )
}

@Composable
private fun NewGameButton(modifier: Modifier = Modifier) {
    Surface(
        onClick = {},
//        color = defaultBoardBgColor,
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.Gray.copy(alpha = 0.4f)),
        content = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.restart_light),
                    contentDescription = "restart",
                )
                Text(
                    text = "New Game",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    )
}