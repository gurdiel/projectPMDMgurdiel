package com.example.tareasapp.addTareas.data.di

import android.content.Context
import androidx.room.Room
import com.example.tareasapp.addTareas.data.TareaDao
import com.example.tareasapp.addTareas.data.TareasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideTareaDao(tareasDatabase: TareasDatabase):TareaDao{
        return tareasDatabase.tareaDao()
    }
    @Provides
    @Singleton
    fun provideTareaDatabase(@ApplicationContext appContext: Context): TareasDatabase{
        return Room.databaseBuilder(appContext, TareasDatabase::class.java, "TareaDatabase").build()
    }
}