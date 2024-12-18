package com.kc.timetrack.dto

import java.time.LocalTime

data class CreateAttendanceDTO(
    val arrivalTime: LocalTime? = null,
    val breakStartTime: LocalTime? = null,
    val breakEndTime: LocalTime? = null,
    val departureTime: LocalTime? = null
)
