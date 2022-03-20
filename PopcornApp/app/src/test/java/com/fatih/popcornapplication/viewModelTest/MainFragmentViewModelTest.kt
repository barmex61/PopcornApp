package com.fatih.popcornapplication.viewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fatih.popcornapplication.MainCoroutineRule
import com.fatih.popcornapplication.getOrAwaitValueTest
import com.fatih.popcornapplication.repositories.FakeModelRepositories
import com.fatih.popcornapplication.resource.Status
import com.fatih.popcornapplication.viewModel.MainFragmentViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainFragmentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRule=MainCoroutineRule()

    private lateinit var viewModel:MainFragmentViewModel

    @Before
    fun setup(){
        viewModel=MainFragmentViewModel(FakeModelRepositories())
    }

    @Test
    fun `search movies without name returns error`(){
        viewModel.search("","Dark",1)
        val response=viewModel.searchList.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `search movies without query returns error`(){
        viewModel.search("tvShow","",1)
        val response=viewModel.searchList.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `search movies with name and query returns success`(){
        viewModel.search("tvShow","Dark",1)
        val response=viewModel.searchList.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `get movies with wrong sort string returns error`(){
        //name_desc dummy
        viewModel.getMovies(1,"name_desc","80")
        val response=viewModel.mostPopularMovies.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `get movies with wrong genres returns error`(){
        //1000 genre id dummy
        viewModel.getMovies(1,"","1000")
        val response=viewModel.mostPopularMovies.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `get tvShow with wrong sort_by returns error`(){
        viewModel.getTvShows(1,"name_desc","80")
        val response=viewModel.mostPopularTvShows.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `get tvShow with wrong genres returns error`(){
        viewModel.getTvShows(1,"","1000")
        val response=viewModel.mostPopularTvShows.getOrAwaitValueTest()
        assertThat(response.status).isEqualTo(Status.ERROR)
    }
}