package es.i12capea.rickypedia.features.characters.character_list.state

sealed class CharacterListStateEvent {
    class GetNextCharacterPage(): CharacterListStateEvent()
}