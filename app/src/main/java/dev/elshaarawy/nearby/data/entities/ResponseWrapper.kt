package dev.elshaarawy.nearby.data.entities


import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.io.IOException

data class ResponseWrapper(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("response")
    val response: JsonElement
) {
    data class Meta(
        @SerializedName("code")
        val code: Int,
        @SerializedName("errorType")
        val errorType: String?,

        @SerializedName("errorDetail")
        val errorDetail: String?
    )

    data class ResponseErrorException(val meta: Meta) : IOException() {
        override val message: String = meta.errorDetail!!
    }
}