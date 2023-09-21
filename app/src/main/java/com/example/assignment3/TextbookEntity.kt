package com.example.assignment3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textbook_table")
data class TextbookEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo(name ="ISBN")
    val isbn:String="",
    @ColumnInfo(name ="title")
    val title: String="",
    @ColumnInfo(name ="author")
    val author: String="",
    @ColumnInfo(name ="associatedCourse")
    val course: String=""
    )

