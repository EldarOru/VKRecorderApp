package com.example.vkrecorderapp.domain.usecases

import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository) {
    operator fun invoke(): Flow<List<AudioNote>> {
        return noteRepository.getNotes()
    }
}