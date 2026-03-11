package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.ContactEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insert(contact: ContactEntity)


    @Update
    suspend fun update(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)


    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<ContactEntity?>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: Int): Flow<ContactEntity?>



}