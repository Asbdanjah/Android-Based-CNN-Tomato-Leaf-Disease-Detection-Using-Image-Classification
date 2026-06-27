package com.asbdanja.tomatoguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.asbdanja.tomatoguard.theme.TomatoGuardTheme
import com.asbdanja.tomatoguard.viewmodel.ScanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     enableEdgeToEdge()
        // Get the ViewModel
        val viewModel = ViewModelProvider(this)[ScanViewModel::class.java]

        // IMPORTANT: Initialize with applicationContext
        viewModel.initialize(applicationContext)

        setContent {
            TomatoGuardTheme {
                TomatoGuardApp(viewModel = viewModel)
            }
        }
    }
}