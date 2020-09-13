package es.i12capea.rickandmortyapiclient.presentation.characters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import kotlinx.android.synthetic.main.character_item.view.*

class CharacterListAdapter(
    private val interaction: Interaction? = null,
    private val requestManager: RequestManager
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {

        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.character_item,
                parent,
                false
            ),
            requestManager,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Character>) {
        differ.submitList(list)
    }

    class CharacterViewHolder
    constructor(
        itemView: View,
        private val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Character) = with(itemView) {

            img_character.apply {
                transitionName = item.id.toString()
                Glide.with(this)
                    .load(item.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }


            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item, img_character)
            }

            itemView.txt_name.text = item.name
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Character, imageView: ImageView)
    }
}