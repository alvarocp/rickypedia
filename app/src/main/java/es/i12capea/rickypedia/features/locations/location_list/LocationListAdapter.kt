package es.i12capea.rickypedia.features.locations.location_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.databinding.LocationItemBinding
import es.i12capea.rickypedia.entities.Location

class LocationListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: LocationItemBinding

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Location>() {

        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(
            binding,
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
        private val binding: LocationItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location) {

            binding.panel.setOnClickListener {
                val navController = it.findNavController()
                if (navController.currentDestination?.id == R.id.locationListFragment) {
                    //Este if arregla el bug del doble click antes de navegar.
                    navController.navigate(R.id.action_locationListFragment_to_locationDetailFragment, bundleOf(Pair("location", item)))
                }
            }

            binding.tvLocationName.text = item.name
            binding.tvLocationDimension.text = item.dimension
            binding.tvLocationType.text = item.type

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Location)
    }

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].id.toLong()
    }
}