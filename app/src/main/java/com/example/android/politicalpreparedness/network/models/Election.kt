package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.example.android.politicalpreparedness.election.model.ElectionModel
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division,
        @ColumnInfo(name = "saved") val saved: Boolean = false
)

fun List<Election>.asDomainModel(): List<ElectionModel> {
        return map {
                ElectionModel(
                        it.id,
                        it.name,
                        it.electionDay,
                        it.division,
                        it.saved
                )
        }
}