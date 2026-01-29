package com.codebythura.fruit2048.ui.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


//@Composable
//fun ActionsRow(modifier: Modifier = Modifier) {
//    Row(
//        modifier = modifier,
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
//    ) {
//        NewGameButton()
//        UndoButton()
//    }
//}
//
//@Composable
//fun NewGameButton(
//    onClick: () -> Unit = {},
//    modifier: Modifier = Modifier
//) {
//    ButtonContainer(
//        onClick = onClick,
//        modifier = modifier,
//        icon = Icons.Default.Add,
//        contentDescription = "Restart"
//    )
//}
//
//@Composable
//fun UndoButton(
//    onClick: () -> Unit = {},
//    modifier: Modifier = Modifier
//) {
//    ButtonContainer(
//        onClick = onClick,
//        modifier = modifier,
//        icon = Icons.Default.Refresh,
//        contentDescription = "Refresh"
//    )
//}

@Composable
private fun ButtonContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon : Int,
    contentDescription: String? = null,
) {
    Surface(
        onClick = onClick,
        color = defaultTileBgColor,
        modifier = modifier.size(58.dp),
        shape = RoundedCornerShape(12.dp),
        content = {
            Image(
                painter = painterResource(icon),
                contentDescription = contentDescription
            )
        }
    )
}