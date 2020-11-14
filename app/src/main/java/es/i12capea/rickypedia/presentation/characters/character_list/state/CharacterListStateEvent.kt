package es.i12capea.rickypedia.presentation.characters.character_list.state

sealed class CharacterListStateEvent {
    class GetNextCharacterPage(): CharacterListStateEvent()
}