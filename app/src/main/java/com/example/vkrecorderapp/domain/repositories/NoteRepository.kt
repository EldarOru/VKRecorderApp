package com.example.vkrecorderapp.domain.repositories

import com.example.vkrecorderapp.domain.entities.AudioNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun getNotes(): Flow<List<AudioNote>>

    suspend fun insertNote(audioNote: AudioNote)

    suspend fun updateNote(audioNote: AudioNote)

    suspend fun deleteNote(audioNote: AudioNote)

    suspend fun getNoteById(id: Int): Flow<AudioNote>
}