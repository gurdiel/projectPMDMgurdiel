package com.example.tareasapp.addTareas.ui.model

data class TareaModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    val tarea: String,
    var selected: Boolean = false)
