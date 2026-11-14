package com.codebythura.fruit2048.ui.game.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.data.AppConsts

private val _playGuideSteps = listOf(
    "Swipe in any direction (⬆\uFE0F⬇\uFE0F⬅\uFE0F➡\uFE0F)",
    "Merge two identical fruits to create a bigger fruit",
    "Each merge increases your score",
    "The game ends when no more moves are possible"
)

@Composable
fun PlayGuideDialog(onDismissRequest : () -> Unit) {
    AppDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "How to Play",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Column(modifier = Modifier.padding(top = 12.dp, bottom = 18.dp)) {
                _playGuideSteps.forEachIndexed { index, string ->
                    Text("${index + 1}. $string")
                }
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("OK")
            }
        }
    }
}