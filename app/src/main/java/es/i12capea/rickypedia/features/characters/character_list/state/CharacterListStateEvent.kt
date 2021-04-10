package es.i12capea.rickypedia.features.characters.character_list.state

sealed class CharacterListStateEvent {
    object GetNextCharacterPage : CharacterListStateEvent()
}