package com.github.gibbrich.airmee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.github.gibbrich.airmee.di.DI
import com.github.gibbrich.airmee.manager.INavigationManager
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationManager: INavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DI.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()

        navigationManager.navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onPause() {
        super.onPause()

        navigationManager.navController = null
    }
}
