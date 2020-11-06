package es.i12capea.rickypedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.rickypedia.domain.exceptions.ResponseException
import es.i12capea.rickypedia.domain.usecases.GetCharacterUseCase
import es.i12capea.rickypedia.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickypedia.presentation.characters.character_detail.CharacterDetailViewModel
import es.i12capea.rickypedia.presentation.characters.character_detail.state.CharacterDetailStateEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
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
class CharacterDetailViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(Dispatchers.Unconfined)

        val episodesUseCase = mockk<GetEpisodesUseCase>()

        MockKAnnotations.init(this)
    }

    @Test
    fun `01 get character succes path`(){
        var loadingCount = 0
        var eventCount = 0

        val getCharacterUseCase = getCharacterUseCase()
        val getEpisodesUseCase = getEpisodesUseCaseDummy()

        val characterDetailViewModel = CharacterDetailViewModel(
            getCharacterUseCase,
            getEpisodesUseCase,
            Dispatchers.Unconfined
        )

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

        assert(eventCount == 2)
        assert(loadingCount == 3)

        coVerify { getCharacterUseCase(1) }
    }

    @Test
    fun `02 get character throw response exception`(){
        var errorCount = 0

        val getCharacterUseCase = getCharacterUseCaseReponseThrow()
        val getEpisodesUseCase = getEpisodesUseCaseDummy()

        val characterDetailViewModel = CharacterDetailViewModel(
            getCharacterUseCase,
            getEpisodesUseCase,
            Dispatchers.Unconfined
        )

        characterDetailViewModel.error.observeForever { event ->
            event.getContentIfNotHandled()?.let{ error ->
                errorCount++
                assert(error.code == 2)
            }
        }

        characterDetailViewModel.setStateEvent(CharacterDetailStateEvent.GetCharacter(1))

        assert(errorCount == 1)
        coVerify { getCharacterUseCase(1) }
    }

    private fun getEpisodesUseCaseDummy() : GetEpisodesUseCase{
        return mockk<GetEpisodesUseCase>()
    }

    private fun getCharacterUseCaseReponseThrow() : GetCharacterUseCase{
        val getCharacterUseCase = mockk<GetCharacterUseCase>()

        coEvery { getCharacterUseCase(1) } throws ResponseException(1, "")

        return getCharacterUseCase
    }





    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        unmockkAll()
    }
}
