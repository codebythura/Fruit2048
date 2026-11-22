package com.codebythura.fruit2048.ui.game.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.R
import com.codebythura.fruit2048.data.AppConsts
import com.codebythura.fruit2048.data.format
import com.codebythura.fruit2048.ui.theme.Fruit2048Theme


@Composable
fun GameOverDialog(
    score: Int,
    onGameRestart: () -> Unit,
) {

    AppDialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .testTag(stringResource(R.string.game_over_dialog))
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Game Over!",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Final score: ${score.format()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp, bottom = 18.dp)
            )
            TryAgainButton(
                onClick = onGameRestart,
                modifier = Modifier.testTag(stringResource(R.string.dialog_try_again_button))
            )
        }
    }

}

@Composable
private fun TryAgainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.restart),
                contentDescription = "restart",
            )
            Text(
                text = "Try Again",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GameOverDialogPreview() {
    Fruit2048Theme {
        GameOverDialog(score = 100) { }
    }
}



