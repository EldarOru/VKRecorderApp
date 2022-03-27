package com.example.vkrecorderapp.data.repositories

import com.example.vkrecorderapp.data.database.NoteDao
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao): NoteRepository {

    override suspend fun getNotes(): Flow<List<AudioNote>> {
        return noteDao.getNotes()
    }

    override suspend fun insertNote(audioNote: AudioNote) {
        noteDao.insertNote(audioNote)
    }

    override suspend fun updateNote(audioNote: AudioNote) {
        noteDao.updateNote(audioNote)
    }

    override suspend fun deleteNote(audioNote: AudioNote) {
        noteDao.deleteNote(audioNote)
    }

    override suspend fun getNoteById(id: Int): Flow<AudioNote> {
        return noteDao.getNoteById(id)
    }
}