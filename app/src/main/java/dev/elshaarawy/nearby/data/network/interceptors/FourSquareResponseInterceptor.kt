package dev.elshaarawy.nearby.data.network.interceptors

import com.google.gson.Gson
import com.google.gson.JsonElement
import dev.elshaarawy.nearby.data.entities.ResponseWrapper
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class FourSquareResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
        .let { originalResponse ->
            originalResponse.takeIf { it.isSuccessful }
                ?.body
                ?.let { originalBody ->
                    val responseWrapper =
                        Gson().fromJson<ResponseWrapper>(
                            originalBody.string(),
                            ResponseWrapper::class.java
                        )
                    if (responseWrapper.meta.code != 200) {
                        throw ResponseWrapper.ResponseErrorException(responseWrapper.meta)
                    } else {
                        originalResponse.mapToNewBody(
                            originalBody.contentType(),
                            responseWrapper.response
                        )
                    }
                } ?: originalResponse
        }

    private fun Response.mapToNewBody(mediaType: MediaType?, jsonElement: JsonElement): Response {
        return this.newBuilder()
            .body(ResponseBody.create(mediaType, jsonElement.toString()))
            .build()
    }
}
