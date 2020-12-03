package es.i12capea.rickypedia.presentation.characters.character_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.databinding.CharacterItemBinding
import es.i12capea.rickypedia.presentation.entities.Character

class CharacterListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding : CharacterItemBinding

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
        binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterViewHolder -> {
                holder.bind(differ.currentList[position])
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
        private val binding : CharacterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character) {
            binding.imgCharacter.apply {
                transitionName = item.id.toString()
                Glide.with(this)
                    .load(item.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }

            binding.txtName.text = item.name

            binding.panel.setOnClickListener {
                val navController = it.findNavController()
                if (navController.currentDestination?.id == R.id.characterListFragment) {
                    //Este if arregla el bug del doble click antes de navegar.
                    val extras = FragmentNavigatorExtras(
                        binding.imgCharacter  to binding.imgCharacter.transitionName
                    )

                    val bundle = Bundle()

                    bundle.putParcelable("character", item)

                    val direction =
                        CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
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

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].id.toLong()
    }
}