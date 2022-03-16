package com.fatih.popcornapplication.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.fatih.popcornapplication.getOrAwaitValue
import com.fatih.popcornapplication.model.RoomEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class RoomDaoTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    @get:Rule
    var hiltRule=HiltAndroidRule(this)

    private lateinit var roomDao: RoomDao
    @Inject
    @Named("testDatabase")
    lateinit var roomDatabase: RoomDb

    @Before
    fun setup(){
        hiltRule.inject()
        roomDao=roomDatabase.roomDao()
    }
    @After
    fun tearDown(){
        roomDatabase.close()
    }
    @Test
    fun insertArtTest()=runBlockingTest {
        val testValue=RoomEntity("22.11.15","Breaking Bad",5.5,true,1)
        roomDao.addTvShow(testValue)
        val list=roomDao.getAllTvShow().getOrAwaitValue()
        assertThat(list).contains(testValue)
    }
    @Test
    fun deleteArtTest()= runBlockingTest {
        val testValue=RoomEntity("22.11.15","Breaking Bad",5.5,true,1)
        roomDao.addTvShow(testValue)
        roomDao.deleteTvShow(testValue)
        val list=roomDao.getAllTvShow().getOrAwaitValue()
        assertThat(list).doesNotContain(testValue)
    }
    @Test
    fun getSelectedArtTest()= runBlockingTest {
        val testValue=RoomEntity("22.11.15","Breaking Bad",5.5,true,1)
        val testValue2=RoomEntity("22.11.15","Breaking",5.5,true,2)
        roomDao.addTvShow(testValue)
        roomDao.addTvShow(testValue2)
        val response=roomDao.getSelectedTvShow(2)
        assertThat(testValue2).isEqualTo(response)
    }
    @Test
    fun getArtWithWrongFiledIdReturnNull()= runBlockingTest{
        val testValue=RoomEntity("22.11.15","Breaking Bad",5.5,true,1)
        roomDao.addTvShow(testValue)
        val response=roomDao.getSelectedTvShow(2)
        assertThat(response).isEqualTo(null)
    }

}