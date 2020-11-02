package es.i12capea.rickandmortyapiclient

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import es.i12capea.rickandmortyapiclient.data.repository.CharacterRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var characterRepository: CharacterRepositoryImpl

    @Test
    fun test(){

    }

}
