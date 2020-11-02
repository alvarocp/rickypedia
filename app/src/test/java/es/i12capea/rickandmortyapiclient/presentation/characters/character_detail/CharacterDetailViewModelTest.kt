package es.i12capea.rickandmortyapiclient.presentation.characters.character_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.getOrAwaitValue
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.entities.LocationShortEntity
import es.i12capea.rickandmortyapiclient.domain.exceptions.RequestException
import es.i12capea.rickandmortyapiclient.domain.exceptions.ResponseException
import es.i12capea.rickandmortyapiclient.domain.repositories.CharacterRepository
import es.i12capea.rickandmortyapiclient.domain.repositories.EpisodeRepository
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharacterUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickandmortyapiclient.presentation.characters.character_detail.state.CharacterDetailStateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.Response
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var characterRepository: CharacterRepository

    @Mock
    lateinit var episodeRepository: EpisodeRepository

    lateinit var getCharacterUseCase: GetCharacterUseCase

    lateinit var getEpisodesUseCase: GetEpisodesUseCase

    lateinit var characterDetailViewModel: CharacterDetailViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        getCharacterUseCase = GetCharacterUseCase(characterRepository)
        getEpisodesUseCase = GetEpisodesUseCase(episodeRepository)

        runBlocking {
            characterOneRepositoryMock()
        }

        characterDetailViewModel = CharacterDetailViewModel(
            getEpisodesUseCase,
            getCharacterUseCase,
            Dispatchers.Unconfined
        )
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `01 test fail if getting character 1 is not character 1`() = runBlocking{
        var loadingCount = 0
        var eventCount = 0
        characterDetailViewModel.isLoading.observeForever {
            loadingCount++
            testLoading(loadingCount, it)
        }

        characterDetailViewModel.dataState.observeForever {
            eventCount++
            when(eventCount){
                1 -> assert(it.getContentIfNotHandled()?.character?.id  == 1) //Emit local
                2 -> assert(it.getContentIfNotHandled()?.character?.id  == 1) //Emit remote
                else -> assert(false)
            }
        }
        characterDetailViewModel.setStateEvent(CharacterDetailStateEvent.GetCharacter(1))
    }

    @Test(expected = ResponseException::class)
    fun `02 fails if not catching response error`(){
        runBlocking {
            Mockito.doThrow(ResponseException(1,""))
                .`when`(characterRepository).getCharacter(2)
        }
        characterDetailViewModel.setStateEvent(CharacterDetailStateEvent.GetCharacter(2))
    }

    @Test
    fun `03 fails if not catching request error`(){

    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    private fun testLoading(loadingCount: Int, it: Boolean){
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

    private suspend fun characterThreeThrowRequestMock(){
        Mockito.`when`(characterRepository.getCharacter(3))
            .thenThrow(RequestException::class.java)
    }

    private suspend fun characterOneRepositoryMock(){
        Mockito.`when`(characterRepository.getCharacter(1))
            .thenReturn(flow {
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
            })
    }

}