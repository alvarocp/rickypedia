package es.i12capea.rickypedia

import es.i12capea.rickypedia.data.local.model.LocalCharacter
import es.i12capea.rickypedia.data.local.model.LocalLocationShort

fun getLocalCharacterSampleId1() : LocalCharacter {
    return LocalCharacter(
        1,
        null,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        LocalLocationShort(1,"Earth (C-137)"),
        LocalLocationShort(20, "Earth (Replacement Dimension)"),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        listOf(1,2,3,4,5,6,7,8)
    )
}

fun getLocalCharacterSampleWithPageId1() : LocalCharacter {
    return LocalCharacter(
        1,
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        LocalLocationShort(1,"Earth (C-137)"),
        LocalLocationShort(20, "Earth (Replacement Dimension)"),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        listOf(1,2,3,4,5,6,7,8,9,10)
    )
}
fun getLocalCharacterList() : List<LocalCharacter>{
    return listOf(
        LocalCharacter(
            1,
            1,
            "Rick Sanchez",
            "Alive",
            "Human",
            "",
            "Male",
            LocalLocationShort(1,"Earth (C-137)"),
            LocalLocationShort(20, "Earth (Replacement Dimension)"),
            "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            listOf(1,2,3,4,5,6,7,8)
        ),
        LocalCharacter(
            2,
            1,
            "Morty Smith",
            "Alive",
            "Human",
            "",
            "Male",
            LocalLocationShort(1,"Earth (C-137)"),
            LocalLocationShort(20, "Earth (Replacement Dimension)"),
            "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            listOf(1,2,3,4,5,6,7,8)
        )
    )
}