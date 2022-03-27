package com.example.vkrecorderapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.vkrecorderapp.R
import com.example.vkrecorderapp.databinding.ActivityMainBinding
import com.example.vkrecorderapp.presentation.fragments.MainListFragment
import com.example.vkrecorderapp.presentation.fragments.OnFragmentInteractionListener
import com.example.vkrecorderapp.presentation.fragments.RecordFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        startPosition()
    }

    private fun startPosition() {
        binding?.mainContainer?.id?.let {
            supportFragmentManager.beginTransaction()
                .replace(it, MainListFragment())
                .commit()
        }

        binding?.bottomContainer?.id?.let {
            supportFragmentManager.beginTransaction()
                .replace(it, RecordFragment())
                .commit()
        }
    }

    override fun onChangeFragment(fragment: Fragment, tag: String) {
        binding?.mainContainer?.id?.let {
            supportFragmentManager.beginTransaction()
                .replace(it, fragment, tag)
                .commit()
        }
    }

    override fun onAddBackStack(name: String, fragment: Fragment) {
        binding?.mainContainer?.id?.let {
            supportFragmentManager.beginTransaction()
                .addToBackStack(name)
                .replace(it, fragment)
                .commit()
        }
    }

    override fun onPopBackStack() {
        for (i in 0..supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    companion object {
        const val mainFragmentTag = "MAIN_FRAGMENT"
        const val favouriteFragmentTag = "DETAILED_FRAGMENT"
    }
}