package es.i12capea

import es.i12capea.domain.entities.CharacterEntity
import es.i12capea.domain.entities.LocationShortEntity
import es.i12capea.domain.usecases.GetCharacterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow

fun getCharacterUseCase() : GetCharacterUseCase {
    val getCharacterUseCase = mockk<GetCharacterUseCase>()

    coEvery { getCharacterUseCase(1) } returns flow {
        emit(getCharacterEntitySampleId1())

        emit(getCharacterEntitySampleId1())
    }
    return getCharacterUseCase
}

fun testLoading(loadingCount: Int, it: Boolean){
    when (loadingCount) {
        1 -> {//Initial value, not loading
            assert(!it)
        }
        2 -> {//Loading
            assert(it)
        }
        3 -> {//Finished
            assert(!it)
        }
        else -> {
            assert(false)
        }
    }
}

fun getCharacterEntitySampleId1() : CharacterEntity{
    return CharacterEntity(
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        LocationShortEntity(1,"Earth (C-137)"),
        LocationShortEntity(20, "Earth (Replacement Dimension)"),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        listOf(1,2,3,4,5,6,7,8)
    )
}