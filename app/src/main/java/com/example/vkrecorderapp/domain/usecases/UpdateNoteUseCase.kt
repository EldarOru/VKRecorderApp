package com.example.vkrecorderapp.domain.usecases

import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.repositories.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(audioNote: AudioNote){
        noteRepository.updateNote(audioNote)
    }
}