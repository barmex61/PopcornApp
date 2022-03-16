package com.fatih.popcornapplication.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fatih.popcornapplication.MainCoroutineRule
import com.fatih.popcornapplication.getOrAwaitValueTest
import com.fatih.popcornapplication.model.RoomEntity
import com.fatih.popcornapplication.repositories.FakeModelRepositories
import com.fatih.popcornapplication.resource.Status
import com.fatih.popcornapplication.viewModel.DetailsFragmentViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@ExperimentalCoroutinesApi
class DetailsFragmentViewModelTests {

    @get:Rule
    var instantTaskExecutorRule= InstantTaskExecutorRule()
    @get:Rule
    var coroutineRule= MainCoroutineRule()

    private lateinit var viewModel: DetailsFragmentViewModel

    @Before
    fun setup(){
        viewModel= DetailsFragmentViewModel(FakeModelRepositories())

    }
    @Test
    fun `add data  into database`(){
        val entity= RoomEntity("rr","ss",5.5,true,5)
        viewModel.addTvShowIntoDatabase(entity)
        val responseStatus= viewModel.controlMessage.getOrAwaitValueTest()
        assertThat(responseStatus.status).isEqualTo(Status.SUCCESS)
        viewModel.deleteTvShowFromDatabase(entity)
    }
    @Test
    fun `add data without some parameters`(){
        val entity= RoomEntity("","ss",5.5,true,6)
        viewModel.addTvShowIntoDatabase(entity)
        val responseStatus= viewModel.controlMessage.getOrAwaitValueTest()
        assertThat(responseStatus.status).isEqualTo(Status.ERROR)
        viewModel.deleteTvShowFromDatabase(entity)
    }
    @Test
    fun `delete data from database`(){
        val entity= RoomEntity("ww","ss",5.5,true,5)
        viewModel.addTvShowIntoDatabase(entity)
        viewModel.deleteTvShowFromDatabase(entity)
        val responseStatus= viewModel.controlMessage.getOrAwaitValueTest()
        assertThat(responseStatus.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `is selected id in database`(){
        val example= RoomEntity("ss","ff",5.5,true,0)
        val example2=RoomEntity("ss","ff",5.5,true,1)
        val example3=RoomEntity("ss","ff",5.5,true,2)
        viewModel.addTvShowIntoDatabase(example)
        viewModel.addTvShowIntoDatabase(example2)
        viewModel.addTvShowIntoDatabase(example3)
        viewModel.isItInDatabase(1)
        val response=viewModel.controlMessage.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `get video urls without query returns error`(){
        viewModel.getVideos("",1)
        val response=viewModel.videos.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `get video urls with query returns success`(){
        viewModel.getVideos("Iron Man",1)
        val response=viewModel.videos.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.SUCCESS)
    }
}