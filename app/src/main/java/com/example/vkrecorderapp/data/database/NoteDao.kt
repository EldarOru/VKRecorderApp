package com.example.vkrecorderapp.data.database

import androidx.room.*
import com.example.vkrecorderapp.domain.entities.AudioNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM audio_notes")
    fun getNotes(): Flow<List<AudioNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: AudioNote)

    @Update
    suspend fun updateNote(note: AudioNote)

    @Delete
    suspend fun deleteNote(note: AudioNote)

}