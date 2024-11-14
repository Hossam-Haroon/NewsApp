package com.example.newsapp.ui.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsap.ui.db.ArticleDatabase

import com.example.newsapp.R
import com.example.newsapp.ui.repository.NewsRepository
import com.example.newsapp.ui.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //val navHostFragment = findViewById<FragmentContainerView>(R.id.nav_host_fragment_container)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
                as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.navController)


        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(newsRepository)
        val viewModelProvider = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[NewsViewModel::class.java]
        viewModel = viewModelProvider





    }

}