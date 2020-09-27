package es.i12capea.rickandmortyapiclient.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import kotlinx.android.synthetic.main.character_item.view.*
import javax.inject.Inject

class CharacterListAdapter @Inject constructor(
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
            requestManager
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
        private val requestManager: RequestManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Character) = with(itemView) {

            img_character.apply {
                transitionName = item.id.toString()
                Glide.with(this)
                    .load(item.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }

            itemView.txt_name.text = item.name

            setOnClickListener {
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.characterListFragment) {

                    val extras = FragmentNavigatorExtras(
                        img_character to img_character.transitionName
                    )

                    val bundle = Bundle()

                    bundle.putParcelable("character", item)

                    val direction = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                        character = item,
                        characterId = item.id,
                        characterName = item.name,
                        characterImage = item.image
                    )

                    navController.navigate(direction, extras)
                }
            }

        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Character, imageView: ImageView)
    }
}