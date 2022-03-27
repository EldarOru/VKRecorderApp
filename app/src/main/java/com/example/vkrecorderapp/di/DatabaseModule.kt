package com.example.vkrecorderapp.di

import android.content.Context
import androidx.room.Room
import com.example.vkrecorderapp.data.database.NoteDatabase
import com.example.vkrecorderapp.data.repositories.NoteRepositoryImpl
import com.example.vkrecorderapp.domain.repositories.NoteRepository
import com.example.vkrecorderapp.domain.usecases.GetNotesUseCase
import com.example.vkrecorderapp.domain.usecases.InsertNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideQuoteDatabase(@ApplicationContext app: Context): NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
            //TODO CHANGE
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao())
    }

    @Provides
    fun provideGetNotesUseCase(rep: NoteRepository): GetNotesUseCase{
        return GetNotesUseCase(rep)
    }

    @Provides
    fun provideInsertQuoteDatabaseUseCase(rep: NoteRepository): InsertNoteUseCase{
        return InsertNoteUseCase(rep)
    }
}