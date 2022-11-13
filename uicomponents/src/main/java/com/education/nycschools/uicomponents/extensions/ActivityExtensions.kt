package com.education.nycschools.uicomponents.extensions

import android.app.Activity
import android.content.Context
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
    context: Context?,
    @IdRes containerId: Int,
    fragment: BaseFragment,
    data: BaseFragmentData
) = supportFragmentManager.replaceFragment(context, containerId, fragment, data)

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