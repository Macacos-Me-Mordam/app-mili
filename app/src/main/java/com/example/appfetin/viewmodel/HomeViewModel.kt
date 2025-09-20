package com.example.appfetin.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfetin.model.Occurrence
import com.example.appfetin.repository.OccurrenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val occurrenceRepository = OccurrenceRepository(application.applicationContext)

    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Idle)
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun submitOccurrence(
        description: String,
        address: String,
        frequency: String,
        imageUri: Uri?
    ) {
        if (imageUri == null) {
            _uiState.value = HomeUIState.Error("Selecione uma imagem.")
            return
        }

        viewModelScope.launch {
            _uiState.value = HomeUIState.Loading

            // 1. Fazer upload da imagem para o S3
            val uploadResult = occurrenceRepository.uploadImageToS3(imageUri)

            uploadResult.fold(
                onSuccess = { imageUrl ->
                    // 2. Enviar ocorrência para a API
                    val occurrence = Occurrence(
                        description = description,
                        photoUrl = imageUrl,
                        address = address,
                        frequency = frequency
                    )
                    val createResult = occurrenceRepository.createOccurrence(occurrence)

                    createResult.fold(
                        onSuccess = {
                            _uiState.value = HomeUIState.Success
                        },
                        onFailure = {
                            _uiState.value = HomeUIState.Error("Falha ao enviar ocorrência: ${it.message}")
                        }
                    )
                },
                onFailure = {
                    _uiState.value = HomeUIState.Error("Falha no upload da imagem: ${it.message}")
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = HomeUIState.Idle
    }
}

sealed class HomeUIState {
    object Idle : HomeUIState()
    object Loading : HomeUIState()
    object Success : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}