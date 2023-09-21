package com.example.assignment3

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TextbookDao {

    @Insert
    suspend fun insert(textbookEntity: TextbookEntity)

    @Update
    suspend fun update(textbookEntity: TextbookEntity)

    @Delete
    suspend fun delete(textbookEntity: TextbookEntity)

    @Query("SELECT * FROM `textbook_table`")
    fun fetchAllBooks(): Flow<List<TextbookEntity>>

    @Query("SELECT * FROM `textbook_table` WHERE id=:id")
    fun fetchBookById(id:Int): Flow<TextbookEntity>

}