package com.example.android.politicalpreparedness.election

import android.location.Address
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.model.ElectionModel
import com.example.android.politicalpreparedness.election.model.asDatabaseModel
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.repository.Result
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val repository: ElectionsRepository,
    val election: ElectionModel,
) : ViewModel() {

    val errorMessage: LiveData<Int?>
        get() = Transformations.map(_electionDetails) {
            if (it is Result.Failure) {
                R.string.failed_load_voter_info
            } else {
                null
            }
        }

    private val _electionDetails: MutableLiveData<Result<State?>> = MutableLiveData()
    val electionDetails: LiveData<State?> = Transformations.map(_electionDetails) {
        when (it) {
            is Result.Success -> it.data
            else -> null
        }
    }

    private val _navigateBack: MutableLiveData<Boolean> = MutableLiveData()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun loadDetails(address: Address?) {
        viewModelScope.launch {
            val exactAddress = "${address?.getAddressLine(0)}"
            val response = repository.getElectionDetails(election.id, exactAddress)
            _electionDetails.value = response
        }
    }

    fun onActionClick() {
        viewModelScope.launch {
            repository.markAsSaved(election.asDatabaseModel(), election.saved.not())
            _navigateBack.value = true
        }
    }

    fun navigateCompleted() {
        _navigateBack.value = false
    }
}