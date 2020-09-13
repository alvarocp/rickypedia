package es.i12capea.rickandmortyapiclient.presentation.characters.state

import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.presentation.entities.Character

sealed class CharactersStateEvent {
    class GetCharacter(val id: Int) : CharactersStateEvent()
    class GetAllCharacters(val page: Int = 1) : CharactersStateEvent()
    class GetEpisodesFromCharacter(val character: Character) : CharactersStateEvent()
    class GetCharacters(val ids: List<Int>) : CharactersStateEvent()
}