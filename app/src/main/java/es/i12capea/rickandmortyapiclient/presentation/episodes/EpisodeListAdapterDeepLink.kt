package es.i12capea.rickandmortyapiclient.presentation.episodes

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.common.navigateUriWithSlideInOptions
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import kotlinx.android.synthetic.main.episode_item.view.*

class EpisodeListAdapterDeepLink() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        return EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.episode_item,
                parent,
                false
            )
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
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Episode) = with(itemView) {

            itemView.tv_title.text = item.name
            itemView.tv_episode.text = item.episode

            setOnClickListener{
                findNavController().navigateUriWithSlideInOptions(
                    Uri.parse("https://www.rickandmortyapiclient.com/episodeDetail/${item.id}")
                )
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Episode)
    }
}