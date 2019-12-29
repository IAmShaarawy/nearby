package dev.elshaarawy.nearby.data.repositories

import android.content.Context
import android.content.SharedPreferences
import dev.elshaarawy.nearby.data.repositories.PreferencesRepository.Companion.NEARBY_PREFERENCES

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
interface PreferencesRepository {

    companion object : (Context) -> PreferencesRepository {
        override fun invoke(ctx: Context): PreferencesRepository = PreferencesRepositoryImpl(ctx)
        const val NEARBY_PREFERENCES = "nearby_preferences"
    }
}

private class PreferencesRepositoryImpl(
    ctx: Context
) : PreferencesRepository {
    private val sharedPreferences by lazy {
        ctx.getSharedPreferences(NEARBY_PREFERENCES, Context.MODE_PRIVATE)
    }
}