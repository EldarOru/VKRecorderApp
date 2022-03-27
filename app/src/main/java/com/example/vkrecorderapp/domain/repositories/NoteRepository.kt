package com.example.vkrecorderapp.domain.repositories

import com.example.vkrecorderapp.domain.entities.AudioNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<AudioNote>>

    suspend fun insertNote(audioNote: AudioNote)

    suspend fun updateNote(audioNote: AudioNote)

    suspend fun deleteNote(audioNote: AudioNote)
}