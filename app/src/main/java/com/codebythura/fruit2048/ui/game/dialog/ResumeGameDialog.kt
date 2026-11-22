package com.codebythura.fruit2048.ui.game.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.R
import com.codebythura.fruit2048.data.AppConsts

@Composable
fun ResumeGameDialog(
    onResume: () -> Unit,
    onNewGame: () -> Unit,
) {
    AppDialog(onDismissRequest = onResume) {
        Column(
            modifier = Modifier
                .testTag(stringResource(R.string.game_resume_dialog))
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Confirm",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Would you like to resume or start a new game?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp, bottom = 18.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResumeGameButton(
                    onClick = onResume,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(stringResource(R.string.resume_game_button))
                )
                NewGameButton(
                    onClick = onNewGame,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(stringResource(R.string.dialog_new_game_button))
                )
            }
        }
    }

}


@Composable
private fun NewGameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
        modifier = modifier,
    ) {
        Text("New Game")
    }
}

@Composable
private fun ResumeGameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
        modifier = modifier,
    ) {
        Text(text = "Resume")
    }
}