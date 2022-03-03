package com.kotlin.mvvm_room.database

import androidx.room.*
import com.kotlin.mvvm_room.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: User) : Long

    @Update
    suspend fun updateUser(user: User) : Int

    @Delete
    suspend fun deleteUser(user: User) : Int

    @Query("DELETE FROM user_table")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<User>>
}