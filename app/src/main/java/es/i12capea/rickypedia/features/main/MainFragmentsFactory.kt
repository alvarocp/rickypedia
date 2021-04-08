package es.i12capea.rickypedia.features.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import es.i12capea.rickypedia.features.characters.character_detail.CharacterDetailFragment
import es.i12capea.rickypedia.features.characters.character_list.CharacterListFragment
import es.i12capea.rickypedia.features.episodes.episode_detail.EpisodeDetailFragment
import es.i12capea.rickypedia.features.episodes.episode_list.EpisodeListFragment
import es.i12capea.rickypedia.features.locations.location_detail.LocationDetailFragment
import es.i12capea.rickypedia.features.locations.location_list.LocationListFragment
import javax.inject.Inject

class MainFragmentsFactory @Inject constructor(
    /*
        Dependencias necesarias para los fragmentos en su constructor
     */
) : FragmentFactory(){

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            LocationListFragment::class.java.name -> {
                LocationListFragment()
            }
            LocationDetailFragment::class.java.name -> {
                LocationDetailFragment()
            }
            EpisodeListFragment::class.java.name -> {
                EpisodeListFragment()
            }
            EpisodeDetailFragment::class.java.name -> {
                EpisodeDetailFragment()
            }
            CharacterListFragment::class.java.name -> {
                CharacterListFragment()
            }
            CharacterDetailFragment::class.java.name -> {
                CharacterDetailFragment()
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}