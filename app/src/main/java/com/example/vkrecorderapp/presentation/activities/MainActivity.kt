package com.example.vkrecorderapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vkrecorderapp.R
import com.example.vkrecorderapp.databinding.ActivityMainBinding
import com.example.vkrecorderapp.presentation.fragments.MainListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.mainContainer?.id?.let {
            supportFragmentManager.beginTransaction()
                .replace(it, MainListFragment())
                .commit()
        }
    }
}