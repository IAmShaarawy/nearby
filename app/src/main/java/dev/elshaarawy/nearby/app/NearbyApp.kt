package dev.elshaarawy.nearby.app

import android.app.Application
import dev.elshaarawy.nearby.injection.RepositoriesModule
import dev.elshaarawy.nearby.injection.DatabaseModule
import dev.elshaarawy.nearby.injection.NetworkModule
import dev.elshaarawy.nearby.injection.ViewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class NearbyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupKoin(this)
    }

    private fun setupKoin(app: Application) {
        startKoin {
            androidContext(app)
            modules(
                listOf(
                    ViewModelsModule(),
                    RepositoriesModule(),
                    DatabaseModule(),
                    NetworkModule()
                )
            )
        }
    }
}