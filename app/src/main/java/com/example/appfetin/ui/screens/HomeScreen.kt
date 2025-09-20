package com.example.appfetin.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appfetin.ui.components.*
import com.example.appfetin.ui.theme.AppFetinTheme
import com.example.appfetin.viewmodel.HomeUIState
import com.example.appfetin.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val frequencyMap = mapOf(
        "one_time" to "Ocorrência Única",
        "recurring" to "Recorrente"
    )
    val frequencyDisplayOptions = frequencyMap.values.toList()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is HomeUIState.Success -> {
                Toast.makeText(context, "Ocorrência enviada com sucesso!", Toast.LENGTH_SHORT).show()
                // Limpa o formulário
                imageUri = null
                address = ""
                description = ""
                frequency = ""
                expanded = false
                homeViewModel.resetState()
            }
            is HomeUIState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                homeViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { AppTopBar(showNavigation = false) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToChat,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Conversar com o bot"
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Seção Principal: Envio de Evidência
                item {
                    EvidenceSection(
                        imageUri = imageUri,
                        address = address,
                        onAddressChange = { address = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        frequency = frequency,
                        onFrequencyChange = { newFrequency -> frequency = newFrequency },
                        frequencyMap = frequencyMap,
                        frequencyDisplayOptions = frequencyDisplayOptions,
                        isExpanded = expanded,
                        onExpandedChange = { expanded = it },
                        onPhotoPickerClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        onClearImage = { imageUri = null },
                        onSubmit = {
                            homeViewModel.submitOccurrence(description, address, frequency, imageUri)
                        },
                        isEnabled = uiState != HomeUIState.Loading
                    )
                }

                // A Seção Secundária (Dica e Progresso) foi removida daqui
            }

            // Overlay de Carregamento
            if (uiState == HomeUIState.Loading) {
                LoadingOverlay()
            }
        }
    }
}

@Composable
private fun EvidenceSection(
    imageUri: Uri?,
    address: String,
    onAddressChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    frequency: String,
    onFrequencyChange: (String) -> Unit,
    frequencyMap: Map<String, String>,
    frequencyDisplayOptions: List<String>,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onPhotoPickerClick: () -> Unit,
    onClearImage: () -> Unit,
    onSubmit: () -> Unit,
    isEnabled: Boolean
) {
    CustomCard {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SectionHeader(title = "Evidência de Descarte", subtitle = "Registre um descarte irregular para análise.")

            if (imageUri == null) {
                ImagePickerPlaceholder(onClick = onPhotoPickerClick, isEnabled = isEnabled)
            } else {
                Box(contentAlignment = Alignment.TopEnd) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Imagem da evidência",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(onClick = onClearImage, enabled = isEnabled) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remover Imagem",
                            tint = Color.White,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .padding(8.dp)
                        )
                    }
                }
            }

            CustomTextField(
                value = address,
                onValueChange = onAddressChange,
                label = "Endereço do local",
                enabled = isEnabled && imageUri != null
            )
            CustomTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = "Descrição da ocorrência",
                maxLines = 3,
                enabled = isEnabled && imageUri != null
            )
            CustomDropdownMenu(
                value = frequencyMap[frequency] ?: "",
                onValueChange = { selectedDisplay ->
                    onFrequencyChange(frequencyMap.entries.find { it.value == selectedDisplay }?.key ?: "")
                },
                label = "Frequência da ocorrência",
                options = frequencyDisplayOptions,
                expanded = isExpanded,
                onExpandedChange = onExpandedChange,
                enabled = isEnabled && imageUri != null
            )

            val isFormValid = address.isNotBlank() && description.isNotBlank() && frequency.isNotBlank()
            CustomButton(
                text = "Enviar Evidência",
                onClick = onSubmit,
                enabled = isEnabled && imageUri != null && isFormValid
            )
        }
    }
}

@Composable
fun ImagePickerPlaceholder(onClick: () -> Unit, isEnabled: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = isEnabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = "Adicionar Foto",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "Toque para adicionar uma foto",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = Color.White)
            Text(
                text = "Enviando...",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppFetinTheme {
        HomeScreen(onNavigateToChat = {})
    }
}