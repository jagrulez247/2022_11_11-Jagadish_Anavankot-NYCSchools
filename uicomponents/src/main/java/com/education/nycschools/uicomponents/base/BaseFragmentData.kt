package com.education.nycschools.uicomponents.base

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.AnimRes

abstract class BaseFragmentData : Parcelable {

    abstract val tagRes: Int
    abstract val deeplinkHostRes: Int

    companion object {
        const val FRAGMENT_DATA = "fragmentData"
        const val ENTER_ANIM = "enterAnim"
        const val EXIT_ANIM = "exitAnim"
        const val IS_UI_TEST = "isUiTest"
        const val ADD_TO_BACK_STACK = "addToBackStack"
    }

    protected val args: Bundle = Bundle()

    fun setUiTest(isTest: Boolean): BaseFragmentData {
        args.apply { putBoolean(IS_UI_TEST, isTest) }
        return this
    }

    fun addToBackStack(): BaseFragmentData {
        args.apply { putBoolean(ADD_TO_BACK_STACK, true) }
        return this
    }

    fun setEnterAnimation(@AnimRes enterAnim: Int): BaseFragmentData {
        args.apply { putInt(ENTER_ANIM, enterAnim) }
        return this
    }

    fun setExitAnimation(@AnimRes exitAnim: Int): BaseFragmentData {
        args.apply { putInt(EXIT_ANIM, exitAnim) }
        return this
    }

    fun build(): Bundle = args
}