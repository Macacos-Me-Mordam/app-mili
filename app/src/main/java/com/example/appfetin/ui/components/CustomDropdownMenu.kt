package com.example.appfetin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            readOnly = true,
            label = { 
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            )
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
