package com.example.tareasapp.addTareas.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tareasapp.addTareas.domain.AddTareaUseCase
import com.example.tareasapp.addTareas.domain.DeleteAllUseCase
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
    private val deleteAllUseCase: DeleteAllUseCase,
    //No es privado porque lo voy a llamar desde la variable de StateFlow
    getTareaUseCase: GetTareasUseCase
) : ViewModel() {
    //Creamos un stateFlow de la sealed Interface.
    //Consume el caso de uso que le da el listado.
    //Recoge error
    val uiState: StateFlow<TareaUiState> = getTareaUseCase().map(::Success)
        .catch { TareaUiState.Error(it) }
        //Espera un tiempo que indicamos en milisegundos para esperar en segundo plano
        //Y cuando pase ese tiempo bloquea el flow. Y el estado inicial es Loading. Saldrá cargando.
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TareaUiState.Loading)

    private val _showDialogo = MutableLiveData<Boolean>()
    val showDialogo: LiveData<Boolean> = _showDialogo

    private val _showAlert = MutableLiveData<Boolean>()
    val showAlert: LiveData<Boolean> = _showAlert

    private val _showConfirmacion = MutableLiveData<Boolean>()
    val showConfirmacion: LiveData<Boolean> = _showConfirmacion

    fun onDialogoCerrar() {
        _showDialogo.value = false
    }

    fun onAlertCerrar() {
        _showAlert.value = false
    }

    fun onConfirmacionCerrar(){
        _showConfirmacion.value = false
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

    fun onMostrarAlertClick() {
        _showAlert.value = true
    }

    fun onMostrarConfirmacionClick(){
        _showConfirmacion.value = true
    }

    fun onCheckBoxSelected(tareaModel: TareaModel) {
        viewModelScope.launch {
            updateTareaUseCase(tareaModel.copy(selected = !tareaModel.selected))
        }
    }

    fun onItemEliminar(tareaModel: TareaModel) {
        _showConfirmacion.value = false
        viewModelScope.launch {
            deleteTareaUseCase(tareaModel)
        }
    }

    fun onItemClear() {
        _showAlert.value = false
        viewModelScope.launch {
            deleteAllUseCase()
        }
    }

    fun onItemEditar(tareaModel: TareaModel) {
        //MEJORA, quedo pdte. por falta de tiempo.
    }
}