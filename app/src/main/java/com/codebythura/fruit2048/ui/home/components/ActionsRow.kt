package com.codebythura.fruit2048.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
fun ActionsRow(
    onStartNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        NewGameButton2(
            onClick = onStartNewGame,
            modifier = Modifier.weight(1f)
        )
        UndoGameButton(modifier = Modifier)
    }
}

@Composable
fun UndoGameButton(modifier: Modifier = Modifier) {
    Surface(
        onClick = {},
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        content = {
            Image(
                painter = painterResource(R.drawable.undo_dark),
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
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.Gray),
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
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    )
}

@Composable
fun NewGameButton2(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.restart_dark),
                contentDescription = "restart",
            )
            Text(
                text = "New Game",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }

}