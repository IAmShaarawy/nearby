package dev.elshaarawy.nearby.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dev.elshaarawy.nearby.data.repositories.PreferencesRepository.Companion.IS_SINGLE_UPDATE
import dev.elshaarawy.nearby.data.repositories.PreferencesRepository.Companion.NEARBY_PREFERENCES

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
interface PreferencesRepository {

    var isSingleUpdate: Boolean

    companion object : (Context) -> PreferencesRepository {
        override fun invoke(ctx: Context): PreferencesRepository = PreferencesRepositoryImpl(ctx)
        const val NEARBY_PREFERENCES = "nearby_preferences"
        const val IS_SINGLE_UPDATE = "is_single_update"
    }
}

private class PreferencesRepositoryImpl(
    ctx: Context
) : PreferencesRepository {
    private val sharedPreferences by lazy {
        ctx.getSharedPreferences(NEARBY_PREFERENCES, Context.MODE_PRIVATE)
    }

    override var isSingleUpdate: Boolean
        get() = sharedPreferences.getBoolean(IS_SINGLE_UPDATE, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_SINGLE_UPDATE, value) }
}