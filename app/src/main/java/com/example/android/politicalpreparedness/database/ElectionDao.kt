package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAll(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAllSync(): List<Election>

    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(election: Election)

    @Query("UPDATE election_table SET saved = 1 WHERE id = :id AND division_id = :divisionId")
    suspend fun markAsSaved(id: Int, divisionId: String)

    @Query("UPDATE election_table SET saved = 0 WHERE id = :id AND division_id = :divisionId")
    suspend fun markAsNotSaved(id: Int, divisionId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(election: List<Election>)

    @Query("SELECT * FROM election_table WHERE id = :id AND division_id = :divisionId LIMIT 1")
    suspend fun get(id: Int, divisionId: String): Election?
}