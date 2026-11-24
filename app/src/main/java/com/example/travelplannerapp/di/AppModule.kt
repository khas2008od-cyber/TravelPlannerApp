package com.example.travelplannerapp.di

import androidx.room.Room
import com.example.travelplannerapp.ListViewModel
import com.example.travelplannerapp.data.AppDatabase
import com.example.travelplannerapp.data.CountriesApi
import com.example.travelplannerapp.data.TripRepositoryImpl
import com.example.travelplannerapp.domain.TripRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<CountriesApi> {
        Retrofit.Builder()
            .baseUrl("https://pw-d20261017a29.plainwork-apps.workers.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }
}

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    single {
        get<AppDatabase>().tripDao()
    }
}

val repositoryModule = module {
    single<TripRepository> {
        TripRepositoryImpl(get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        ListViewModel(get())
    }
}

val appModule = module {
    includes(
        networkModule,
        dbModule,
        repositoryModule,
        viewModelModule
    )
}