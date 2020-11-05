package es.i12capea.rickypedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.rickypedia.domain.entities.CharacterEntity
import es.i12capea.rickypedia.domain.entities.LocationShortEntity
import es.i12capea.rickypedia.domain.exceptions.RequestException
import es.i12capea.rickypedia.domain.exceptions.ResponseException
import es.i12capea.rickypedia.domain.usecases.GetCharacterUseCase
import es.i12capea.rickypedia.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickypedia.presentation.characters.character_detail.CharacterDetailViewModel
import es.i12capea.rickypedia.presentation.characters.character_detail.state.CharacterDetailStateEvent
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4::class)
class UseCaseUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockKAnnotations.init(this)
    }

    @Test
    fun `01`() {
        var loadingCount = 0
        var eventCount = 0

        val getCharacterUseCase = mockk<GetCharacterUseCase>()
        val episodesUseCase = mockk<GetEpisodesUseCase>()

        coEvery { getCharacterUseCase(2) } throws RequestException()

        coEvery { getCharacterUseCase(1) } returns flow {
            emit(CharacterEntity(
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
            ))

            emit(CharacterEntity(
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
            ))
        }

        val characterDetailViewModel = CharacterDetailViewModel(getCharacterUseCase,episodesUseCase)

        characterDetailViewModel.dataState.observeForever {
            eventCount++
            when(eventCount){
                1 -> assert(it.getContentIfNotHandled()?.character?.id  == 1) //Emit local
                2 -> assert(it.getContentIfNotHandled()?.character?.id  == 1) //Emit remote
                else -> assert(false)
            }
        }

        characterDetailViewModel.setStateEvent(CharacterDetailStateEvent.GetCharacter(2))

        coVerify { getCharacterUseCase(2) }
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        unmockkAll()
    }
}
