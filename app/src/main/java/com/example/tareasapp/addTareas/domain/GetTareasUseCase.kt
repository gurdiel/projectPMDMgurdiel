package com.example.tareasapp.addTareas.domain

import com.example.tareasapp.addTareas.data.TareaRepository
import com.example.tareasapp.addTareas.ui.model.TareaModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTareasUseCase @Inject constructor(private val tareaRepository: TareaRepository) {
    operator fun invoke(): Flow<List<TareaModel>> = tareaRepository.tareas
}