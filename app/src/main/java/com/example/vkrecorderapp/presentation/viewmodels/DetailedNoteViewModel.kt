package com.example.vkrecorderapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.usecases.GetNoteByIdUseCase
import com.example.vkrecorderapp.domain.usecases.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase): ViewModel() {

    private val _noteState = MutableStateFlow(AudioNote())
    val noteState: StateFlow<AudioNote>
        get() = _noteState

    fun getNote(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getNoteByIdUseCase.invoke(id)
                .collect {
                    _noteState.value = it
                }
        }
    }

    fun updateNote(audioNote: AudioNote){
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUseCase.invoke(audioNote)
        }
    }
}