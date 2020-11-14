package es.i12capea.rickypedia.presentation.characters.character_detail.state

import es.i12capea.rickypedia.presentation.entities.Character

sealed class CharacterDetailStateEvent {
    class GetCharacter(val id: Int) : CharacterDetailStateEvent()
    class GetEpisodesFromCharacter(val character: Character) : CharacterDetailStateEvent()
}