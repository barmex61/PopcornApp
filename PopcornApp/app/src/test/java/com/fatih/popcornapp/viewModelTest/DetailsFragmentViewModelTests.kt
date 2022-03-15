package com.fatih.popcornapp.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fatih.popcornapp.MainCoroutineRule
import com.fatih.popcornapp.getOrAwaitValueTest
import com.fatih.popcornapp.model.RoomEntity
import com.fatih.popcornapp.repositories.FakeModelRepositories
import com.fatih.popcornapp.resource.Status
import com.fatih.popcornapp.viewModel.DetailsFragmentViewModel
import com.google.common.truth.Truth
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
        Truth.assertThat(responseStatus.status).isEqualTo(Status.SUCCESS)
        viewModel.deleteTvShowFromDatabase(entity)
    }
    @Test
    fun `delete data from database`(){
        val entity= RoomEntity("ww","ss",5.5,true,5)
        viewModel.addTvShowIntoDatabase(entity)
        viewModel.deleteTvShowFromDatabase(entity)
        val responseStatus= viewModel.controlMessage.getOrAwaitValueTest()
        Truth.assertThat(responseStatus.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `is selected id in database`(){
        val example= RoomEntity("ss","ff",5.5,true,4)
        viewModel.addTvShowIntoDatabase(example)
        viewModel.isItInDatabase(4)
        val response=viewModel.controlMessage.getOrAwaitValueTest()
        Truth.assertThat(response.status).isEqualTo(Status.SUCCESS)
        viewModel.deleteTvShowFromDatabase(example)
    }
}