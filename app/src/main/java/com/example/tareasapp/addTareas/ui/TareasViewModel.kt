package com.example.tareasapp.addTareas.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tareasapp.addTareas.domain.AddTareaUseCase
import com.example.tareasapp.addTareas.domain.DeleteTareaUseCase
import com.example.tareasapp.addTareas.domain.GetTareasUseCase
import com.example.tareasapp.addTareas.domain.UpdateTareaUseCase
import com.example.tareasapp.addTareas.ui.TareaUiState.Success
import com.example.tareasapp.addTareas.ui.model.TareaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareasViewModel @Inject constructor(
    private val addTareaUseCase: AddTareaUseCase,
    private val updateTareaUseCase: UpdateTareaUseCase,
    private val deleteTareaUseCase: DeleteTareaUseCase,
    getTareaUseCase: GetTareasUseCase
): ViewModel() {

    val uiState:StateFlow<TareaUiState> = getTareaUseCase().map (::Success )
        .catch { TareaUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TareaUiState.Loading)

    private val _showDialogo = MutableLiveData<Boolean>()
    val showDialogo:LiveData<Boolean> = _showDialogo

    fun onDialogoCerrar() {
        _showDialogo.value = false
    }

    fun onTareaCreated(tarea: String) {
        _showDialogo.value = false
        viewModelScope.launch {
            addTareaUseCase(TareaModel(tarea = tarea))
        }
    }

    fun onMostrarDialogoClick() {
        _showDialogo.value = true
    }

    fun onCheckBoxSelected(tareaModel: TareaModel) {

        viewModelScope.launch {
            updateTareaUseCase(tareaModel.copy(selected = !tareaModel.selected))
        }



    }

    fun onItemEliminar(tareaModel: TareaModel) {
        viewModelScope.launch {
            deleteTareaUseCase(tareaModel)
        }
    }

    fun onItemEditar(tareaModel: TareaModel) {
        //MEJORA
    }
}