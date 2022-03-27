package com.example.vkrecorderapp.domain.usecases

import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Flow<AudioNote> {
        return noteRepository.getNoteById(id)
    }
}