package es.i12capea.rickandmortyapiclient.presentation.episodes.state

import android.os.Parcelable
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.Page

data class EpisodesViewState(
    var lastPage: Page<Episode>? = null,
    var characters: List<Character>? = null,
    var character: Character? = null,
    var layoutManagerState: Parcelable? = null,
    var episodes: List<Episode>? = null,
    var episode: Episode? = null
)