package com.example.donacionesjava.ws_manager

data class ApiResponse<T>(
    val OK: Boolean,
    val message: String,
    val data: T
)
