package com.fatih.popcornapplication.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.fatih.popcornapplication.R
import com.fatih.popcornapplication.launchFragmentInHiltContainer
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

        launchFragmentInHiltContainer<MainFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.shareImage)).perform(click())
        Mockito.verify(navController).navigate(MainFragmentDirections.actionMainFragmentToWatchListFragment())
    }

}