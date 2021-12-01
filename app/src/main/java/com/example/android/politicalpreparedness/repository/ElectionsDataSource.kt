package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.State

interface ElectionDataSource {
    fun observerElections(): LiveData<Result<List<Election>>>
    suspend fun getElections(): Result<List<Election>>
    suspend fun saveElections(elections: List<Election>)
    suspend fun markAsSaved(election: Election)
    suspend fun deleteAllElections()
    suspend fun getElectionDetails(electionId: Int, address: String): Result<State?>
    suspend fun getRepresentatives(address: Address): Result<RepresentativeResponse>
}