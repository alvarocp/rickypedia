package es.i12capea.data

import es.i12capea.rickypedia.shared.domain.entities.CharacterEntity
import es.i12capea.rickypedia.shared.domain.entities.LocationShortEntity
import es.i12capea.rickypedia.shared.domain.exceptions.ResponseException
import es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow

fun getCharacterRepositoryResponseException() : es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository {
    val mockCharacterRepository = mockk<es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository>()

    coEvery { mockCharacterRepository.getCharacter(any()) } throws es.i12capea.rickypedia.shared.domain.exceptions.ResponseException(
        1,
        ""
    )

    return mockCharacterRepository
}

fun getCharacterRepository() : es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository {
    val mockCharacterRepository = mockk<es.i12capea.rickypedia.shared.domain.repositories.CharacterRepository>()

    coEvery { mockCharacterRepository.getCharacter(1) } returns flow {
        emit(getCharacterEntitySampleId1())

        emit(getCharacterEntitySampleId1())
    }

    return mockCharacterRepository
}

fun getCharacterEntitySampleId1() : es.i12capea.rickypedia.shared.domain.entities.CharacterEntity {
    return es.i12capea.rickypedia.shared.domain.entities.CharacterEntity(
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        es.i12capea.rickypedia.shared.domain.entities.LocationShortEntity(1, "Earth (C-137)"),
        es.i12capea.rickypedia.shared.domain.entities.LocationShortEntity(
            20,
            "Earth (Replacement Dimension)"
        ),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        listOf(1, 2, 3, 4, 5, 6, 7, 8)
    )
}