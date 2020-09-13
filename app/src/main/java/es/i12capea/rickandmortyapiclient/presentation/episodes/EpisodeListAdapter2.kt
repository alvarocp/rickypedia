package es.i12capea.rickandmortyapiclient.presentation.episodes

import androidx.recyclerview.widget.RecyclerView
import es.i12capea.rickandmortyapiclient.R.*
import es.i12capea.rickandmortyapiclient.presentation.BaseListAdapter
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import kotlinx.android.synthetic.main.episode_item.view.*

class EpisodeListAdapter2(interact: Interaction<Episode>? = null) : BaseListAdapter<Episode>(
    interact,
    itemsSame = { old, new -> old.id == new.id },
    contentsSame = { old, new -> old == new }
) {

    override val itemLayout: Int = layout.episode_item

    override fun bindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        interaction: Interaction<Episode>?
    ) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            interaction?.onItemSelected(holder.absoluteAdapterPosition, item)
        }

        holder.itemView.tv_title.text = item.name
        holder.itemView.tv_episode.text = item.episode
    }
}