package com.example.vkrecorderapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.usecases.DeleteNoteUseCase
import com.example.vkrecorderapp.domain.usecases.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase): ViewModel() {

    init {
        getNotes()
    }

    private val _notesState = MutableStateFlow(listOf<AudioNote>())
    val notesState: StateFlow<List<AudioNote>>
        get() = _notesState


    private fun getNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            getNotesUseCase.invoke()
                .collect {
                    _notesState.value = it
                }
        }
    }

    fun deleteNote(audioNote: AudioNote) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase.invoke(audioNote)
        }
    }
}