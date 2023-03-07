package android.ncdev.basektornetwork.core.main

import android.ncdev.basektornetwork.R
import android.ncdev.basektornetwork.core.base.BaseActivity
import android.os.Bundle
import android.ncdev.basektornetwork.databinding.ActivityMainBinding
import android.ncdev.common.utils.viewbinding.viewBinding
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavigation()
    }

    private fun initNavigation(){
        navController = (supportFragmentManager.findFragmentById(binding.navFragmentContainer.id) as NavHostFragment)
            .navController
        with(navController){
            graph = navInflater.inflate(R.navigation.nav_main)
            binding.bottomBar.setupWithNavController(this)
        }

    }
}