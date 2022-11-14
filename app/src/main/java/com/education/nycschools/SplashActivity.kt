package com.education.nycschools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.education.nycschools.navigation.MainNavActivity
import com.education.nycschools.uicomponents.base.BaseFragmentData.Companion.FRAGMENT_DATA
import com.education.nycschools.uicomponents.extensions.launchActivityNew
import com.education.nycschools.uicomponents.extensions.observe
import com.education.nycschools.uicomponents.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(com.education.nycschools.R.layout.activity_main)
        supportActionBar?.hide()
        observe(viewModel.uiState()) { updateUiState(it) }
        viewModel.onCreate()
    }

    private fun updateUiState(state: SplashUiStates) {
        when (state) {
            is SplashUiStates.MoveToAppScreens -> {
                launchActivityNew<MainNavActivity>(FRAGMENT_DATA, state.data)
            }
            SplashUiStates.Finish -> finish()
        }
    }
}