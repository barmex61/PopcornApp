package com.fatih.popcornapplication.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.adapter.MovieAdapter
import com.fatih.popcornapplication.launchFragmentInHiltContainer
import com.fatih.popcornapplication.model.ResultMovies
import com.fatih.popcornapplication.repositories.FakeModelRepositoriesTest
import com.fatih.popcornapplication.viewModel.MainFragmentViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MainFragmentTest {

    @get:Rule
    var hiltRule=HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @Inject
    lateinit var fragmentFactory:FragmentFactories
    @Before
    fun setup(){
        hiltRule.inject()
    }
    @Test
    fun testNavigationMainFragmentToDetailsFragment(){
        val navController=Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<MainFragment>(factory =  fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(withId(R.id.shareImage)).perform(click())
        Mockito.verify(navController).navigate(MainFragmentDirections.actionMainFragmentToWatchListFragment())
    }
    @Test
    fun searchMovie(){
        val navController=Mockito.mock(NavController::class.java)
        val testViewModel=MainFragmentViewModel(FakeModelRepositoriesTest())
        val resultMovies=ResultMovies(true,"s", listOf(),1,"s","s","s",2.2,"/wdE6ewaKZHr62bLqCn7A2DiGShm.jpg","s","s",true,2.2,1)
        val resultMovies1=ResultMovies(true,"s", listOf(),1,"s","s","s",2.2,"/wdE6ewaKZHr62bLqCn7A2DiGShm.jpg","s","s",true,2.2,1)
        val resultMovies2=ResultMovies(true,"s", listOf(),1,"s","s","s",2.2,"/wdE6ewaKZHr62bLqCn7A2DiGShm.jpg","s","s",true,2.2,1)
        val resultMovies3=ResultMovies(true,"s", listOf(),1,"s","s","s",2.2,"/wdE6ewaKZHr62bLqCn7A2DiGShm.jpg","s","s",true,2.2,1)
        launchFragmentInHiltContainer<MainFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
            viewModel=testViewModel
            movieAdapter.movieList= listOf(resultMovies,resultMovies1,resultMovies2,resultMovies3)
        }
        Espresso.onView(withId(R.id.moviesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.MovieViewHolder>(2,
            click()))
        Mockito.verify(navController).navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(resultMovies.id,-518144
            ,false))
        launchFragmentInHiltContainer<DetailsFragment>(factory = fragmentFactory){
            assertThat(arguments?.let {
                DetailsFragmentArgs.fromBundle(it).id
            }).isEqualTo(resultMovies.id)
        }

    }

}