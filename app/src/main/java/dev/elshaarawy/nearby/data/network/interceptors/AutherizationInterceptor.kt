package dev.elshaarawy.nearby.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class AutherizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authorizedUrl = chain.request()
            .url
            .newBuilder()
            .addQueryParameter(CLIENT_ID_KEY, CLIENT_ID)
            .addEncodedQueryParameter(CLIENT_SECRET_KEY, CLIENT_SECRET)
            .addEncodedQueryParameter(VERSION_KEY, VERSION)
            .build()
        return chain.request()
            .newBuilder()
            .url(authorizedUrl)
            .build()
            .let(chain::proceed)
    }

    companion object {
        private const val CLIENT_ID_KEY = "client_id"
        private const val CLIENT_ID = "CGAGFMB4131AOZI02A1JR2KYQQYJTMVF1JCNR3VSHKPW5050"
        private const val CLIENT_SECRET_KEY = "client_secret"
        private const val CLIENT_SECRET = "CKQ4HYFGULIQ2M4KTTQKZN54H3MMOOXFO3TY5OJ4YDC5ADXP"
        private const val VERSION_KEY = "v"
        private const val VERSION = "20191229"

    }
}