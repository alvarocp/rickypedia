package es.i12capea.rickandmortyapiclient.presentation.characters.character_list.state

sealed class CharacterListStateEvent {
    class GetNextCharacterPage(): CharacterListStateEvent()
}