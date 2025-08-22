package com.example.appfetin.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appfetin.ui.theme.AppFetinTheme
import com.example.appfetin.ui.theme.*
import com.example.appfetin.ui.components.CustomButton
import com.example.appfetin.ui.components.CustomCard
import com.example.appfetin.ui.components.CustomTextField
import com.example.appfetin.ui.components.CustomDropdownMenu
import com.example.appfetin.ui.components.AppTopBar
import com.example.appfetin.ui.components.ButtonVariant

private val dailyTips = listOf(
    "Prefira pontos de coleta e descarte o que não é lixo comum (pilhas, baterias, eletrônicos).",
    "Leve sua garrafa ou caneca: você reduz plástico e economiza dinheiro.",
    "Organize o lixo em seco e úmido. Facilita a coleta e evita mau cheiro.",
    "Doe o que ainda tem uso. Reutilizar é melhor do que reciclar.",
    "Fotografe descartes irregulares para apoiar a limpeza do bairro."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipOfTheDay = remember { dailyTips.random() }

    var evidenceCount by remember { mutableStateOf(0) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showForm by remember { mutableStateOf(false) }
    
    val frequencyOptions = listOf(
        "Uma vez (ocorrência isolada)",
        "Diariamente",
        "Semanalmente", 
        "Mensalmente",
        "Esporadicamente"
    )
    
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            imageUri = uri
            if (uri != null) {
                showForm = true
            }
        }
    )

    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = { AppTopBar(showNavigation = false) },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    CustomButton(
                        text = "Conversar com o bot",
                        onClick = onNavigateToChat,
                        variant = ButtonVariant.Primary
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CustomCard(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = AccentAmber.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Dica",
                            tint = AccentAmber,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Dica do Dia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tipOfTheDay,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                        )
                    }
                }
            }

            CustomCard(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = SuccessGreen.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = "Progresso",
                            tint = SuccessGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Seu Progresso",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Evidências enviadas:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = SuccessGreen.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = evidenceCount.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }
                }
            }

            CustomCard(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = AccentBlue.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Evidência",
                                tint = AccentBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Evidência de Descarte",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Registre um descarte irregular (lixo fora do lugar, entulho, resíduos perigosos) e envie a foto.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                            )
                        }
                    }
                    
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Imagem da evidência",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (imageUri == null) {
                        CustomButton(
                            text = "Escolher Foto da Galeria",
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            variant = ButtonVariant.Secondary
                        )
                    }
                    
                    if (showForm && imageUri != null) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                        
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Detalhes da Ocorrência",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Preencha as informações abaixo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        CustomTextField(
                            value = address,
                            onValueChange = { newAddress -> address = newAddress },
                            label = "Endereço do local"
                        )
                        
                        CustomTextField(
                            value = description,
                            onValueChange = { newDescription -> description = newDescription },
                            label = "Descrição da ocorrência",
                            maxLines = 3
                        )
                        
                        CustomDropdownMenu(
                            value = frequency,
                            onValueChange = { newFrequency -> frequency = newFrequency },
                            label = "Frequência da ocorrência",
                            options = frequencyOptions,
                            expanded = expanded,
                            onExpandedChange = { newExpanded -> expanded = newExpanded }
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    imageUri = null
                                    showForm = false
                                    address = ""
                                    description = ""
                                    frequency = ""
                                    expanded = false
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Cancelar",
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (showForm && imageUri != null && address.isNotBlank() && description.isNotBlank() && frequency.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    CustomButton(
                        text = "Enviar Evidência",
                        onClick = {
                            evidenceCount++
                            imageUri = null
                            showForm = false
                            address = ""
                            description = ""
                            frequency = ""
                            expanded = false
                        },
                        variant = ButtonVariant.Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppFetinTheme {
        HomeScreen(
            onNavigateToChat = {}
        )
    }
}