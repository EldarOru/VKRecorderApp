package com.example.vkrecorderapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkrecorderapp.domain.entities.AudioNote

@Database(entities = [AudioNote::class], version = 2)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object{
        const val DATABASE_NAME = "notes"
    }

}