package me.cniekirk.trackbuddy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextRadioButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = modifier.clickable { onSelected() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}