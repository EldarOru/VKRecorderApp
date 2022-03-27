package com.example.vkrecorderapp.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_notes")
data class AudioNote(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val description: String,
    val date: String,
    @ColumnInfo(name = "storage_path") val storagePath: String
)