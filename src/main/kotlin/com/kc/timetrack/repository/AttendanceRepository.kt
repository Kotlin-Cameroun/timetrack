package com.kc.timetrack.repository

import com.kc.timetrack.models.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface AttendanceRepository: JpaRepository<Attendance, UUID> {
    fun findByEmployeeId(employeeId: UUID?): List<Attendance>

    fun findByEmployeeIdAndAttendanceDate(employeeId: UUID, attendanceDate: LocalDate): Attendance?

    fun findByEmployeeIdAndAttendanceDateBetween(employeeId: UUID, startDate: LocalDate, endDate: LocalDate): List<Attendance>

    fun findByAttendanceDateBetween(startDate: LocalDate, endDate: LocalDate): List<Attendance>
}