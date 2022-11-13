package com.education.nycschools.uicomponents.extensions

import android.content.Context
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.base.BaseFragmentData

fun <T> Fragment.observe(data: LiveData<T>, observer: Observer<in T>) {
    data.observe(viewLifecycleOwner, observer)
}

fun BaseFragment.replaceFragment(
    context: Context?,
    @IdRes containerId: Int,
    fragment: BaseFragment,
    data: BaseFragmentData
) = parentFragmentManager.replaceFragment(context, containerId, fragment, data)

internal fun FragmentManager.replaceFragment(
    context: Context?,
    @IdRes containerId: Int,
    fragment: BaseFragment,
    data: BaseFragmentData
) {
    this
        .takeIf { !it.isDestroyed }
        ?.apply {
            val tag = context?.getString(data?.tagRes ?: 0) ?: ""
            val args = data?.build()
            val enterAnim = args?.getInt(BaseFragmentData.ENTER_ANIM, 0) ?: 0
            val exitAnim = args?.getInt(BaseFragmentData.EXIT_ANIM, 0) ?: 0
            val addBackSTack = args?.getBoolean(BaseFragmentData.ADD_TO_BACK_STACK, false) ?: false
            beginTransaction().apply {
                if (enterAnim != 0 || exitAnim != 0) setCustomAnimations(enterAnim, exitAnim)
                setReorderingAllowed(true)
                replace(containerId, fragment, tag)
                if (addBackSTack) addToBackStack(tag)
                commitAllowingStateLoss()
            }
        }
}