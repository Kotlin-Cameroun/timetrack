package com.kc.timetrack.dto

import java.time.LocalTime
import java.util.UUID

data class UpdateAttendanceDTO(
    val attendanceId: UUID,
    val arrivalTime: LocalTime? = null,
    val breakStartTime: LocalTime? = null,
    val breakEndTime: LocalTime? = null,
    val departureTime: LocalTime? = null
)
