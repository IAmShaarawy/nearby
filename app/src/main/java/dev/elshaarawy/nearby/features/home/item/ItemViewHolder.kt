package dev.elshaarawy.nearby.features.home.item

import androidx.recyclerview.widget.RecyclerView
import dev.elshaarawy.nearby.data.entities.ExploreResponse
import dev.elshaarawy.nearby.data.entities.ExploreResponse.Group.Item
import dev.elshaarawy.nearby.databinding.ItemNearbyBinding
import timber.log.Timber

/**
 * @author Mohamed Elshaarawy on Dec 29, 2019.
 */
class ItemViewHolder(private val itemNearbyBinding: ItemNearbyBinding) :
    RecyclerView.ViewHolder(itemNearbyBinding.root) {

    fun bind(item: Item) {
        itemNearbyBinding.item = item
        Timber.e(item.venue.location.fullAddress)
    }
}