package com.example.a19mart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a19mart.ui.fragments.RootNodeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RootNodeFragment.newInstance(1))
                .commitNow()
        }
    }
}