package com.fatih.popcornapplication.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.getOrAwaitValue
import com.fatih.popcornapplication.launchFragmentInHiltContainer
import com.fatih.popcornapplication.model.RoomEntity
import com.fatih.popcornapplication.repositories.FakeModelRepositoriesTest
import com.fatih.popcornapplication.resource.Status
import com.fatih.popcornapplication.viewModel.DetailsFragmentViewModel
import com.fatih.popcornapplication.viewModel.WatchListFragmentViewModel
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
class DetailsFragmentTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    var hiltRule=HiltAndroidRule(this)
    @Inject
    lateinit var fragmentFactory: FragmentFactories
    @Before
    fun setup(){
        hiltRule.inject()

    }
    @Test
    fun testNavigationDetailsFragmentToWatchListFragment(){

        val navController=Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<DetailsFragment>(factory = fragmentFactory) {
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.onView(ViewMatchers.withId(R.id.shareButton)).perform(click())
        Mockito.verify(navController).navigate(DetailsFragmentDirections.actionDetailsFragmentToWatchListFragment())

    }
    @Test
    fun onBackPressed(){
        val navController=Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<DetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()
    }
    @Test
    fun testAddWatchList(){
        val entity=RoomEntity("22","22",2.2,true,1)
        val testViewModel=DetailsFragmentViewModel(FakeModelRepositoriesTest())

        launchFragmentInHiltContainer<DetailsFragment>(factory = fragmentFactory){
            viewModel=testViewModel
            roomEntity= entity
        }

        Espresso.onView(ViewMatchers.withId(R.id.watchList)).perform(click())
        val response=testViewModel.controlMessage.getOrAwaitValue()
        assertThat(response.status).isEqualTo(Status.SUCCESS)
    }

}