package com.example.appfetin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appfetin.ui.theme.*

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    val shape = RoundedCornerShape(16.dp)
    
    val backgroundBrush = when (variant) {
        ButtonVariant.Primary -> Brush.horizontalGradient(
            colors = listOf(
                if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                if (enabled) PrimaryBrownLight else MaterialTheme.colorScheme.outline
            )
        )
        ButtonVariant.Secondary -> Brush.horizontalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.secondaryContainer
            )
        )
        ButtonVariant.Outline -> Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                Color.Transparent
            )
        )
    }
    
    val contentColor = when (variant) {
        ButtonVariant.Primary -> MaterialTheme.colorScheme.onPrimary
        ButtonVariant.Secondary -> MaterialTheme.colorScheme.onSecondaryContainer
        ButtonVariant.Outline -> MaterialTheme.colorScheme.primary
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = if (variant == ButtonVariant.Primary) 6.dp else 0.dp,
                shape = shape,
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
            .clip(shape)
            .background(backgroundBrush)
            .then(
                if (variant == ButtonVariant.Outline) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = shape
                    )
                } else Modifier
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor.copy(alpha = if (enabled) 1f else 0.6f),
            fontWeight = FontWeight.SemiBold
        )
    }
}

enum class ButtonVariant {
    Primary,
    Secondary,
    Outline
}
