package dev.elshaarawy.nearby.data.network.interceptors

import android.app.Application
import android.content.Context
import dev.elshaarawy.nearby.extensions.isConnected
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference

class ConnectivityInterceptor(private val ctx: WeakReference<Context>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (ctx.get() !is Application)
            throw IllegalArgumentException("You mast provide an application context to don't cause memory leaks")
        return ctx.get()?.takeIf { it.isConnected() }?.run {
            return@run chain.request().run(chain::proceed)
        } ?: throw ConnectivityException()
    }

    class ConnectivityException : IOException("Connection Not Found Exception")
}