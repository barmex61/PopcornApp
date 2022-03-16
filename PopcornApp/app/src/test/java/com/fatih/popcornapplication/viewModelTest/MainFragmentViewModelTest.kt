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
}