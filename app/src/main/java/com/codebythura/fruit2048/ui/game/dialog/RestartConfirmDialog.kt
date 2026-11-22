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
fun RestartConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    AppDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .testTag(stringResource(R.string.restart_confirm_dialog))
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
                text = "Are you sure to restart the game?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp, bottom = 18.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CancelButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(stringResource(R.string.cancel_button))
                )
                RestartButton(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(stringResource(R.string.confirm_button))
                )
            }
        }
    }

}


@Composable
private fun RestartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
        modifier = modifier,
    ) {
        Text("Restart")
    }
}

@Composable
private fun CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
        modifier = modifier,
    ) {
        Text(text = "Cancel")
    }
}