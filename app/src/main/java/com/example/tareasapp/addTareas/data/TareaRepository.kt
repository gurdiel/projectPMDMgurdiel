package com.example.tareasapp.addTareas.data

import com.example.tareasapp.addTareas.ui.model.TareaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TareaRepository @Inject constructor(private val tareaDao: TareaDao) {

    val tareas: Flow<List<TareaModel>> = tareaDao.getTareas().map { items ->
        items.map {
            TareaModel(
                it.id,
                it.tarea,
                it.selected
            )
        }
    }//maper transformando datos entre capas.

    suspend fun add(tareaModel: TareaModel) {
        tareaDao.addTarea(tareaModel.toData())
    }
    suspend fun update(tareaModel: TareaModel){
        tareaDao.updateTarea(tareaModel.toData())
    }
    suspend fun delete(tareaModel: TareaModel){
        tareaDao.deleteTarea(tareaModel.toData())
    }

    /**
     * Eliminamos toda las entradas de la tabla.
     */
    suspend fun deleteAll(){
        tareaDao.deleteAll()
    }

}

fun TareaModel.toData():TareaEntity{
    return TareaEntity(this.id,this.tarea,this.selected)
}