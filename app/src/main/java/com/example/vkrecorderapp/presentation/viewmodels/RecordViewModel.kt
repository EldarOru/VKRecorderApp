package com.example.vkrecorderapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrecorderapp.domain.entities.AudioNote
import com.example.vkrecorderapp.domain.usecases.InsertNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val insertNoteUseCase: InsertNoteUseCase): ViewModel() {

    fun insertNote(audioNote: AudioNote){
        viewModelScope.launch(Dispatchers.IO) {
            insertNoteUseCase.invoke(audioNote)
        }
    }

}