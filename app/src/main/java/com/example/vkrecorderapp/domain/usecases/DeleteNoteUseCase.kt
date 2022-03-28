package com.example.vkrecorderapp.domain.usecases

import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(audioNote: AudioNote) {
        return noteRepository.deleteNote(audioNote)
    }
}