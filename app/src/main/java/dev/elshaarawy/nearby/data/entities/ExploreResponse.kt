package dev.elshaarawy.nearby.data.entities


import com.google.gson.annotations.SerializedName

data class ExploreResponse(
    @SerializedName("suggestedFilters")
    val suggestedFilters: SuggestedFilters,
    @SerializedName("suggestedRadius")
    val suggestedRadius: Int,
    @SerializedName("headerLocation")
    val headerLocation: String,
    @SerializedName("headerFullLocation")
    val headerFullLocation: String,
    @SerializedName("headerLocationGranularity")
    val headerLocationGranularity: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("suggestedBounds")
    val suggestedBounds: SuggestedBounds,
    @SerializedName("groups")
    val groups: List<Group>
) {
    @Transient
    var coordinates: Coordinates? = null

    data class SuggestedFilters(
        @SerializedName("header")
        val header: String,
        @SerializedName("filters")
        val filters: List<Filter>
    ) {
        data class Filter(
            @SerializedName("name")
            val name: String,
            @SerializedName("key")
            val key: String
        )
    }

    data class SuggestedBounds(
        @SerializedName("ne")
        val ne: Ne,
        @SerializedName("sw")
        val sw: Sw
    ) {
        data class Ne(
            @SerializedName("lat")
            val lat: Double,
            @SerializedName("lng")
            val lng: Double
        )

        data class Sw(
            @SerializedName("lat")
            val lat: Double,
            @SerializedName("lng")
            val lng: Double
        )
    }

    data class Group(
        @SerializedName("type")
        val type: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("items")
        val items: List<Item>
    ) {
        data class Item(
            @SerializedName("venue")
            val venue: Venue,
            @SerializedName("referralId")
            val referralId: String
        ) {

            data class Venue(
                @SerializedName("id")
                val id: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("location")
                val location: Location,
                @SerializedName("categories")
                val categories: List<Category>,
                @SerializedName("photos")
                val photos: Photos
            ) {
                data class Location(
                    @SerializedName("address")
                    val address: String,
                    @SerializedName("crossStreet")
                    val crossStreet: String,
                    @SerializedName("lat")
                    val lat: Double,
                    @SerializedName("lng")
                    val lng: Double,
                    @SerializedName("labeledLatLngs")
                    val labeledLatLngs: List<LabeledLatLng>,
                    @SerializedName("distance")
                    val distance: Int,
                    @SerializedName("cc")
                    val cc: String,
                    @SerializedName("city")
                    val city: String,
                    @SerializedName("state")
                    val state: String,
                    @SerializedName("country")
                    val country: String,
                    @SerializedName("formattedAddress")
                    val formattedAddress: List<String>
                ) {
                    data class LabeledLatLng(
                        @SerializedName("label")
                        val label: String,
                        @SerializedName("lat")
                        val lat: Double,
                        @SerializedName("lng")
                        val lng: Double
                    )
                }

                data class Category(
                    @SerializedName("id")
                    val id: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("pluralName")
                    val pluralName: String,
                    @SerializedName("shortName")
                    val shortName: String,
                    @SerializedName("icon")
                    val icon: Icon,
                    @SerializedName("primary")
                    val primary: Boolean
                ) {
                    data class Icon(
                        @SerializedName("prefix")
                        val prefix: String,
                        @SerializedName("suffix")
                        val suffix: String
                    )
                }

                data class Photos(
                    @SerializedName("count")
                    val count: Int,
                    @SerializedName("groups")
                    val groups: List<Any>
                )
            }
        }
    }
}