package com.example.a19mart.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a19mart.R
import com.example.a19mart.ui.fragments.NodeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NodeFragment.newInstance(1))
                .commitNow()
        }
    }
}