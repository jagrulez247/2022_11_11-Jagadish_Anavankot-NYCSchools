package com.education.nycschools.domain.models

data class ApiError(
    val status_code: Int = 0,
    val status_message: String? = null
)