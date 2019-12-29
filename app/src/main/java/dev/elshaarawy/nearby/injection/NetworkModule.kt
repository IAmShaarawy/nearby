package dev.elshaarawy.nearby.injection

import dev.elshaarawy.nearby.BuildConfig
import dev.elshaarawy.nearby.data.network.interceptors.AutherizationInterceptor
import dev.elshaarawy.nearby.data.network.interceptors.ConnectivityInterceptor
import dev.elshaarawy.nearby.data.network.interceptors.FourSquareResponseInterceptor
import dev.elshaarawy.nearby.data.network.services.VenueService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
object NetworkModule : () -> Module {
    private const val BASE_URL = "https://api.foursquare.com/v2/"
    override fun invoke(): Module = module {
        single { ConnectivityInterceptor(WeakReference(androidContext())) }
        single { FourSquareResponseInterceptor() }
        single { AutherizationInterceptor() }
        single {
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Timber.e(message)
                }

            }).apply {

                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        }
        single {
            OkHttpClient.Builder()
                .run {
                    addInterceptor(get<ConnectivityInterceptor>())
                    addInterceptor(get<AutherizationInterceptor>())
                    addInterceptor(get<FourSquareResponseInterceptor>())
                    addInterceptor(get<HttpLoggingInterceptor>())
                    connectTimeout(10, TimeUnit.SECONDS)
                    readTimeout(20, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    build()
                }
        }

        single {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(get())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        factory { get<Retrofit>().create(VenueService::class.java) } bind VenueService::class
    }
}