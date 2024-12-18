package com.kc.timetrack.services

import com.kc.timetrack.dto.CreateAttendanceDTO
import com.kc.timetrack.dto.UpdateAttendanceDTO
import com.kc.timetrack.models.Attendance
import com.kc.timetrack.models.Employee
import com.kc.timetrack.repository.AttendanceRepository
import com.kc.timetrack.repository.EmployeRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Service
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val employeeRepository: EmployeRepository
) {

    private val minTime = LocalTime.of(6, 0)  // 6h00
    private val maxTime = LocalTime.of(22, 0) // 22h00

    private fun parseAndValidateTime(time: LocalTime?): LocalTime? {
        if (time == null) return null
        if (time.isBefore(minTime) || time.isAfter(maxTime)) {
            throw IllegalArgumentException("Hours must be between 6:00 and 22:00")
        }
        return time
    }

    // intervalle [6h00, 22h00]
    private fun isTimeValid(time: LocalTime?): Boolean {
        return time != null && !time.isBefore(minTime) && !time.isAfter(maxTime)
    }

    fun getAllAttendances(): List<Attendance> = attendanceRepository.findAll()

    fun getAllAttendanceByEmployeId(employeeId: UUID): List<Attendance> {
        return attendanceRepository.findByEmployeeId(employeeId)
    }

    fun getAllAttendanceByEmail(email: String): List<Attendance> {
        val employee = employeeRepository.findByEmail(email)
            ?: throw IllegalArgumentException("No employee found with email $email")
        return attendanceRepository.findByEmployeeId(employee.id)
    }

    // nouvelle attendance avec validation des horaires
    fun addAttendance(dto: CreateAttendanceDTO, employee: Employee): Attendance {
        try {

            val arrivalTime = parseAndValidateTime(dto.arrivalTime)
            val breakStartTime = parseAndValidateTime(dto.breakStartTime)
            val breakEndTime = parseAndValidateTime(dto.breakEndTime)
            val departureTime = parseAndValidateTime(dto.departureTime)

            // Création de l'entité Attendance
            val attendance = Attendance(
                id = null,
                employee = employee,
                attendanceDate = LocalDate.now(),
                arrivalTime = arrivalTime,
                breakStartTime = breakStartTime,
                breakEndTime = breakEndTime,
                departureTime = departureTime
            )

            return attendanceRepository.save(attendance)
        } catch (ex: Exception) {
            throw RuntimeException("Error adding presence : ${ex.message}")
        }
    }


    fun updateAttendance(attendanceId: UUID, dto: UpdateAttendanceDTO, employee: Employee): Attendance? {
        return try {
            val existingAttendance = attendanceRepository.findById(attendanceId).orElse(null)

            if (existingAttendance == null || existingAttendance.employee.id != employee.id) {
                return null
            }

            val arrivalTime = dto.arrivalTime?.let { parseAndValidateTime(it) }
            val breakStartTime = dto.breakStartTime?.let { parseAndValidateTime(it) }
            val breakEndTime = dto.breakEndTime?.let { parseAndValidateTime(it) }
            val departureTime = dto.departureTime?.let { parseAndValidateTime(it) }

            val updatedAttendance = existingAttendance.copy(
                arrivalTime = arrivalTime ?: existingAttendance.arrivalTime,
                breakStartTime = breakStartTime ?: existingAttendance.breakStartTime,
                breakEndTime = breakEndTime ?: existingAttendance.breakEndTime,
                departureTime = departureTime ?: existingAttendance.departureTime
            )

            attendanceRepository.save(updatedAttendance)
        } catch (ex: Exception) {
            throw RuntimeException("Error updating attendance: ${ex.message}")
        }
    }


    fun deleteAttendance(attendanceId: UUID): Boolean {
        return if (attendanceRepository.existsById(attendanceId)) {
            attendanceRepository.deleteById(attendanceId)
            true
        } else {
            false
        }
    }

    fun getAttendancesByDateRange(employeeId: UUID, startDate: LocalDate, endDate: LocalDate): List<Attendance> {
        return attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(employeeId, startDate, endDate)
    }

    // Récupérer toutes les attendances entre deux dates
    fun getAllAttendancesByDateRange(startDate: LocalDate, endDate: LocalDate): List<Attendance> {
        return attendanceRepository.findByAttendanceDateBetween(startDate, endDate)
    }

}