package com.education.nycschools.uicomponents.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.education.nycschools.uicomponents.base.BaseActivity
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.base.BaseFragmentData.Companion.ADD_TO_BACK_STACK
import com.education.nycschools.uicomponents.base.BaseFragmentData.Companion.ENTER_ANIM
import com.education.nycschools.uicomponents.base.BaseFragmentData.Companion.EXIT_ANIM

fun BaseActivity.replaceFragment(
    @IdRes containerId: Int,
    fragment: BaseFragment,
    data: BaseFragmentData?
) {
    supportFragmentManager
        .takeIf { !it.isDestroyed }
        ?.apply {
            val tag = getString(data?.tagRes ?: 0)
            val args = data?.build()
            val enterAnim = args?.getInt(ENTER_ANIM, 0) ?: 0
            val exitAnim = args?.getInt(EXIT_ANIM, 0) ?: 0
            val addBackSTack = args?.getBoolean(ADD_TO_BACK_STACK, false) ?: false
            beginTransaction().apply {
                if (enterAnim != 0 || exitAnim != 0) setCustomAnimations(enterAnim, exitAnim)
                setReorderingAllowed(true)
                replace(containerId, fragment, tag)
                if (addBackSTack) addToBackStack(tag)
                commitAllowingStateLoss()
            }
        }
}

fun <T> AppCompatActivity.observe(data: LiveData<T>, observer: Observer<in T>) {
    data.observe(this, observer)
}

inline fun <reified T> Activity.launchActivityNew(key: String, data: Parcelable?) = startActivity(
    Intent(this, T::class.java)
        .apply { putExtras(Bundle().apply { putParcelable(key, data) }) }
)

inline fun <reified T> Activity.launchActivity() = startActivity(
    Intent(this, T::class.java)
)