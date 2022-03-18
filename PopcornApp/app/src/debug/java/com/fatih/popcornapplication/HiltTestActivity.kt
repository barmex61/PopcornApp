package com.fatih.popcornapplication

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity:AppCompatActivity() {
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        // super.onSaveInstanceState(outState, outPersistentState)
    }
}