package com.example.assignment3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TextbookEntity::class], version = 1)
abstract class TextbookDatabase: RoomDatabase(){

    abstract  fun textbookDao(): TextbookDao

    companion object{
        @Volatile
        private var INSTANCE: TextbookDatabase? = null

        fun getInstance(context: Context):TextbookDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(INSTANCE == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TextbookDatabase::class.java,
                        "textbook_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance!!
            }

        }
    }
}