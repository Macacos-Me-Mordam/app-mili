package com.example.appfetin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    gradient: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = modifier
        .fillMaxWidth()
        .shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(20.dp),
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .clip(RoundedCornerShape(20.dp))
        .then(
            if (gradient) {
                Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
            } else {
                Modifier.background(MaterialTheme.colorScheme.surface)
            }
        )
        .then(
            if (onClick != null) {
                Modifier.clickable { onClick() }
            } else Modifier
        )
        .padding(24.dp)

    Column(
        modifier = cardModifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}
