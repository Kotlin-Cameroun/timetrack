package com.kc.timetrack.dto

import java.util.UUID

data class EmployeeRequest(
    val name: String,
    val email: String,
    val password: String
)

data class EmployeeResponse(
    val id: UUID,
    val name: String,
    val email: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
