package es.i12capea.rickandmortyapiclient.presentation.characters.character_detail.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Character

sealed class CharacterDetailStateEvent {
    class GetCharacter(val id: Int) : CharacterDetailStateEvent()
    class GetEpisodesFromCharacter(val character: Character) : CharacterDetailStateEvent()
}