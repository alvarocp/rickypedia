package es.i12capea.rickypedia

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.i12capea.data.local.RymDatabase
import es.i12capea.data.local.dao.LocalCharacterDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class LocalCharacterDaoTest {
//
//    private val testDispatcher = TestCoroutineDispatcher()
//    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: es.i12capea.data.local.RymDatabase
    private lateinit var dao: es.i12capea.data.local.dao.LocalCharacterDao

    @Before
    fun setup(){

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            es.i12capea.data.local.RymDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.getLocalCharacterDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test(expected = SQLiteConstraintException::class)
    fun `01_insert_same_character_twice_constraint_exception`() = runBlocking {
        val localCharacter = getLocalCharacterSampleId1()
        dao.insertCharacterOrAbort(localCharacter)
        val character = dao.searchCharacterById(1)
        assert(character?.id == 1)
        dao.insertCharacterOrAbort(localCharacter)
        assert(true)
    }

    @Test
    fun `02_insert_character_list_with_already_inserted_character`() = runBlocking {
        val localCharacter = getLocalCharacterSampleId1()
        dao.insertCharacterOrAbort(localCharacter)
        var character = dao.searchCharacterById(1)
        assert(character?.pageId == null)
        assert(character?.episodes?.size == 8)
        
        val characterWithPage = getLocalCharacterSampleWithPageId1()
        dao.insertOrUpdate(characterWithPage)
        character = dao.searchCharacterById(1)
        assert(character?.pageId == 1)
        assert(character?.episodes?.size == 10)
    }
}