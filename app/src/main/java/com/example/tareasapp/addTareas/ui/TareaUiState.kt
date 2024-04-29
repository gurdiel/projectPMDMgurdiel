package com.example.tareasapp.addTareas.ui

import com.example.tareasapp.addTareas.ui.model.TareaModel

sealed interface TareaUiState {
    object Loading:TareaUiState
    data class Error(val throwable: Throwable):TareaUiState
    data class Success(val tareas:List<TareaModel>):TareaUiState
}