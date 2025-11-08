package com.example.ipetrecomendationdemo

import android.app.Application
import com.example.ipetrecomendationdemo.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class iPetRecomendationDemo : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Monte a lista e só adiciona o mock em debug
        val modulesList = mutableListOf(
            appModule,
        )

        startKoin {
            androidContext(this@iPetRecomendationDemo)
            modules(modulesList) // aceita List<Module>
        }
    }
}