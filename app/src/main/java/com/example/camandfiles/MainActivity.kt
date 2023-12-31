package com.example.camandfiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, CamFragment())
        }
    }

    fun showList(){
        supportFragmentManager.commit {
            replace(R.id.fragment_container, ListFragment())
            addToBackStack(null)
        }
    }
}