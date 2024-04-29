package com.example.tareasapp.addTareas.domain

import com.example.tareasapp.addTareas.data.TareaRepository
import com.example.tareasapp.addTareas.ui.model.TareaModel
import javax.inject.Inject

class DeleteTareaUseCase @Inject constructor(private val tareaRepository: TareaRepository) {

    suspend operator fun invoke(tareaModel: TareaModel) { tareaRepository.delete(tareaModel)}

}