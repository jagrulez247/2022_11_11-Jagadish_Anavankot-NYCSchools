package com.education.nycschools.domain.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.annotations.TestOnly

object NetworkDispatcher {

    private var testDispatcher: CoroutineDispatcher? = null

    @TestOnly
    fun setTestDispatcher(dispatcher: CoroutineDispatcher) {
        testDispatcher = dispatcher
    }

    fun io(): CoroutineDispatcher = testDispatcher ?: Dispatchers.IO

    fun main(): CoroutineDispatcher = testDispatcher ?: Dispatchers.Main
}