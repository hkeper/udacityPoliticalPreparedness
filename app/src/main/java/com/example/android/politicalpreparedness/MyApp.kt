package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.LocalDataSource
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.election.model.ElectionModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.NetworkDataSource
import com.example.android.politicalpreparedness.repository.ElectionDataSource
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.text.DateFormat
import java.util.*

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            viewModel { (election: ElectionModel) ->
                VoterInfoViewModel(get(), election)
            }
            viewModel { ElectionsViewModel(get()) }
            viewModel { RepresentativeViewModel(get()) }
            single { ElectionDatabase.getInstance(this@MyApp).electionDao as ElectionDao }
            single { CivicsApi.retrofitService as CivicsApiService }
            single(qualifier = named("local")) { LocalDataSource(get(), Dispatchers.IO) as ElectionDataSource }
            single(qualifier = named("remote")) { NetworkDataSource(get(), Dispatchers.IO) as ElectionDataSource }
            single { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) as DateFormat}
            single {
                ElectionsRepository(
                    get<ElectionDataSource>(qualifier = named("local")),
                    get<ElectionDataSource>(qualifier = named("remote")),
                    Dispatchers.IO,
                ) as ElectionsRepository
            }
        }
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(module))
        }
    }
}