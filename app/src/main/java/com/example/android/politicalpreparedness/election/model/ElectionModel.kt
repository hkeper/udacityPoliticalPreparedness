package com.example.android.politicalpreparedness.election.model

import android.os.Parcelable
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ElectionModel(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division,
    val saved: Boolean
) : Parcelable

fun ElectionModel.asDatabaseModel() =
    Election(
        id = id,
        name = name,
        electionDay = electionDay,
        division = division,
        saved = saved
    )
