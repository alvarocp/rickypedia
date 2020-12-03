package es.i12capea.rickypedia.presentation.episodes.episode_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.databinding.EpisodeItemBinding
import es.i12capea.rickypedia.presentation.entities.Episode

class EpisodeListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding : EpisodeItemBinding

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Episode>() {

        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {

            return ( oldItem.id == newItem.id
                    && oldItem.episode == newItem.episode
                    && oldItem.name == newItem.name
                    && oldItem.air_date == newItem.air_date )
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(
            binding,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EpisodeViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Episode>) {
        differ.submitList(list)
    }

    class EpisodeViewHolder
    constructor(
        private val binding: EpisodeItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Episode)  {

            binding.tvTitle.text = item.name
            binding.tvEpisode.text = item.episode

            binding.panel.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("episode", item)
                it.findNavController().navigate(R.id.action_episodeListFragment_to_episodeDetailFragment, bundle)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Episode)
    }

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].id.toLong()
    }

}