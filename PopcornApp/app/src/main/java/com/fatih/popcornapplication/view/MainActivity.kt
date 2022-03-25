package com.fatih.popcornapplication.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.fatih.popcornapplication.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory:FragmentFactories

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(null)
        supportFragmentManager.fragmentFactory=fragmentFactory
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

    }
}