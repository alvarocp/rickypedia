package es.i12capea.rickandmortyapiclient.presentation.locations.location_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import kotlinx.android.synthetic.main.location_item.view.*

class LocationListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Location>() {

        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return LocationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.location_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Location>) {
        differ.submitList(list)
    }

    class LocationViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Location) = with(itemView) {

            setOnClickListener{
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.locationListFragment) {
                    //Este if arregla el bug del doble click antes de navegar.
                    navController.navigate(R.id.action_locationListFragment_to_locationDetailFragment, bundleOf(Pair("location", item)))
                }
            }

            tv_location_name.text = item.name
            tv_location_dimension.text = item.dimension
            tv_location_type.text = item.type

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Location)
    }
}