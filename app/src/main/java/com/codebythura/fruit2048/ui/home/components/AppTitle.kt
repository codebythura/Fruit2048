package com.codebythura.fruit2048.ui.home.components

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onTrophyClicked: () -> Unit,
    onThemeSwitched: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = { AppTitle() },
        modifier = modifier,
        actions = {
            IconButton(onClick = onThemeSwitched) {
                Icon(
                    imageVector = Icons.Filled.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}


@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "2048",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = "Fruit Edition!",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}