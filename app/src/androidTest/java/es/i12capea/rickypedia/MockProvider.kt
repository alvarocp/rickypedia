package es.i12capea.rickypedia

import es.i12capea.data.local.model.LocalCharacter
import es.i12capea.data.local.model.LocalLocationShort

fun getLocalCharacterSampleId1() : es.i12capea.data.local.model.LocalCharacter {
    return es.i12capea.data.local.model.LocalCharacter(
        1,
        null,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        es.i12capea.data.local.model.LocalLocationShort(1, "Earth (C-137)"),
        es.i12capea.data.local.model.LocalLocationShort(20, "Earth (Replacement Dimension)"),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        listOf(1, 2, 3, 4, 5, 6, 7, 8)
    )
}

fun getLocalCharacterSampleWithPageId1() : es.i12capea.data.local.model.LocalCharacter {
    return es.i12capea.data.local.model.LocalCharacter(
        1,
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        es.i12capea.data.local.model.LocalLocationShort(1, "Earth (C-137)"),
        es.i12capea.data.local.model.LocalLocationShort(20, "Earth (Replacement Dimension)"),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    )
}
fun getLocalCharacterList() : List<es.i12capea.data.local.model.LocalCharacter>{
    return listOf(
        es.i12capea.data.local.model.LocalCharacter(
            1,
            1,
            "Rick Sanchez",
            "Alive",
            "Human",
            "",
            "Male",
            es.i12capea.data.local.model.LocalLocationShort(1, "Earth (C-137)"),
            es.i12capea.data.local.model.LocalLocationShort(20, "Earth (Replacement Dimension)"),
            "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ),
        es.i12capea.data.local.model.LocalCharacter(
            2,
            1,
            "Morty Smith",
            "Alive",
            "Human",
            "",
            "Male",
            es.i12capea.data.local.model.LocalLocationShort(1, "Earth (C-137)"),
            es.i12capea.data.local.model.LocalLocationShort(20, "Earth (Replacement Dimension)"),
            "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    )
}