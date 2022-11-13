package com.education.nycschools.uicomponents.custom.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import com.education.nycschools.uicomponents.databinding.LayoutSearchBarBinding
import com.education.nycschools.uicomponents.extensions.hideKeyboard
import com.education.nycschools.uicomponents.extensions.showKeyboardForced


class NycSchoolSearchView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutSearchBarBinding = LayoutSearchBarBinding.inflate(LayoutInflater.from(context))

    init {
        isClickable = true
        visibility = View.VISIBLE
        addView(binding.root)
    }

    fun showKeyboard() {
        binding.searchView.showKeyboardForced()
    }

    fun hideKeyboard() {
        binding.searchView.hideKeyboard()
    }

    fun setSearchQuery(query: String) {
        binding.searchView.setQuery(query, true)
    }

    fun listenToQueryChanges(
            lifecycle: Lifecycle,
            searchCallback: (String?) -> Unit,
            debounceTime: Long = 500L
    ) = binding
            .searchView
            .setOnQueryTextListener(
                    DebouncingQueryTextListener(lifecycle) { searchCallback(it) }
                            .apply { debouncePeriod = debounceTime }
            )
}

