package com.example.tareasapp.addTareas.domain

import com.example.tareasapp.addTareas.data.TareaRepository
import com.example.tareasapp.addTareas.ui.model.TareaModel
import javax.inject.Inject

class DeleteAllUseCase @Inject constructor(private val tareaRepository: TareaRepository) {
    suspend operator fun invoke(){ tareaRepository.deleteAll() }
}