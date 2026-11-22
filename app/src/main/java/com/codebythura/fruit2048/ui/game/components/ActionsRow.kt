package com.codebythura.fruit2048.ui.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.R
import com.codebythura.fruit2048.data.AppConsts

private val buttonHeight = 50.dp

@Composable
fun ActionsRow(
    undoEnabled: Boolean = true,
    onUndo: () -> Unit,
    startNewGameEnabled: Boolean = true,
    onStartNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        NewGameButton(
            enabled = startNewGameEnabled,
            onClick = onStartNewGame,
            modifier = Modifier
                .weight(1f)
                .testTag(stringResource(R.string.new_game_button))
        )
        UndoGameButton(
            enabled = undoEnabled,
            onClick = onUndo,
            modifier = Modifier.testTag(stringResource(R.string.undo_btn))
        )
    }
}

@Composable
fun UndoGameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ActionButton(
        enabled = enabled,
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.size(buttonHeight),
    ) {
        Icon(
            painter = painterResource(R.drawable.undo),
            contentDescription = stringResource(R.string.undo),
        )
    }
}

@Composable
fun NewGameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    ActionButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.restart),
                contentDescription = stringResource(R.string.restart),
            )
            Text(
                text = stringResource(R.string.new_game),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ActionButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(AppConsts.defaultCornerRadius),
        modifier = modifier.height(buttonHeight),
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        ),
        content = content,
    )
}