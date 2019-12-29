package dev.elshaarawy.nearby.injection

import dev.elshaarawy.nearby.data.repositories.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
object RepositoriesModule : () -> Module {
    override fun invoke(): Module = module {
        single { PreferencesRepository(androidContext()) }
    }
}