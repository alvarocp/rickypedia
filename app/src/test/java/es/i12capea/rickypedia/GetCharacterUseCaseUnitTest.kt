package es.i12capea.rickypedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.domain.exceptions.ResponseException
import es.i12capea.domain.usecases.GetCharacterUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JUnit4::class)
class GetCharacterUseCaseUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        Dispatchers.setMain(Dispatchers.Unconfined)

        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        unmockkAll()
    }

    @Test
    fun `01 success test`() = runBlocking{
        val characterRepository = getCharacterRepository()
        val getCharacterUseCase = GetCharacterUseCase(characterRepository)
        var characterCollected = 0

        getCharacterUseCase(1)
            .collect {
                characterCollected++
                assert(it.id == 1)
            }

        assert(characterCollected == 2)

        coVerify { characterRepository.getCharacter(any()) }
    }

    @Test(expected = ResponseException::class)
    fun `02 repository throw response exception`() = runBlocking{
        val characterRepository = getCharacterRepositoryResponseException()
        val getCharacterUseCase = GetCharacterUseCase(characterRepository)

        getCharacterUseCase(1)
            .collect { //Empty collect need because cold stream are not sent if no one is listening.
                 }

        coVerify { characterRepository.getCharacter(1) }
    }

}