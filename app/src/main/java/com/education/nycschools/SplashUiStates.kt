package com.education.nycschools

import com.education.nycschools.uicomponents.base.BaseFragmentData

sealed class SplashUiStates {
    class MoveToAppScreens(val data: BaseFragmentData): SplashUiStates()
    object Finish: SplashUiStates()
}