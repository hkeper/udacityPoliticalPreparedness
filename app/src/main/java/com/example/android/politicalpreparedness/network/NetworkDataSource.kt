package com.example.android.politicalpreparedness.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.ElectionDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.example.android.politicalpreparedness.repository.Result

class NetworkDataSource(
    private val apiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher,
) : ElectionDataSource {

    private val _upcomingElections: MutableLiveData<Result<List<Election>>> = MutableLiveData()

    override suspend fun getElections(): Result<List<Election>> {
        return withContext(ioDispatcher) {
            val result = try {
                Result.Success(apiService.getElections().elections)
            } catch (ex: Exception) {
                Result.Failure(ex)
            }
            _upcomingElections.postValue(result)
            result
        }
    }

    override suspend fun getElectionDetails(electionId: Int, address: String): Result<State?> {
        return withContext(ioDispatcher) {
            val result = try {
                Result.Success(apiService.getVoterInfo(address, electionId).state?.first())
            } catch (ex: Exception) {
                ex.printStackTrace()
                Result.Failure(ex)
            }
            result
        }
    }

    override suspend fun getRepresentatives(address: Address): Result<RepresentativeResponse> {
        return withContext(ioDispatcher) {
            try {
                Result.Success(apiService.getRepresentatives(address.toFormattedString()))
            } catch (ex: java.lang.Exception) {
                Result.Failure(ex)
            }
        }
    }

    override fun observerElections(): LiveData<Result<List<Election>>> {
        return _upcomingElections
    }

    override suspend fun saveElections(elections: List<Election>) {
//        do nothing
    }

    override suspend fun markAsSaved(election: Election) {
//        do nothing
    }

    override suspend fun deleteAllElections() {
//        do nothing
    }
}