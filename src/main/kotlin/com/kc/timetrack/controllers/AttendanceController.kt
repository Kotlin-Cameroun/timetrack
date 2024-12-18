package com.kc.timetrack.controllers

import com.kc.timetrack.config.JwtService
import com.kc.timetrack.dto.CreateAttendanceDTO
import com.kc.timetrack.dto.UpdateAttendanceDTO
import com.kc.timetrack.models.Attendance
import com.kc.timetrack.repository.EmployeRepository
import com.kc.timetrack.services.AttendanceService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/attendances")
class AttendanceController(
    private val attendanceService: AttendanceService,
    private val employeRepository: EmployeRepository,
    private val jwtService: JwtService
) {

    @GetMapping("/all")
    fun getAttendances(): ResponseEntity<List<Attendance>> {
        val attendances = attendanceService.getAllAttendances()
        return ResponseEntity.ok(attendances)
    }

    @GetMapping("/employe")
    fun getAttendancesForLoggedEmployee(request: HttpServletRequest): ResponseEntity<List<Any>> {
        // Extraire le token "Authorization" de la requête
        val authHeader = request.getHeader("Authorization") ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val token = authHeader.substring(7)
        val email = jwtService.extractUsername(token)  // Extraire l'email depuis le token
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        println("Email: $email")
        val attendances = attendanceService.getAllAttendanceByEmail(email)
        return ResponseEntity.ok(attendances)
    }

    @PostMapping("/save")
    fun saveAttendance(
        @RequestBody dto: CreateAttendanceDTO,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        try {
            val authHeader = request.getHeader("Authorization") ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token")
            if (!authHeader.startsWith("Bearer ")) {
                println("token error")
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")
            }

            val token = authHeader.substring(7)

            val email = jwtService.extractUsername(token) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user")

            // Récupération de l'objet Employee
            val employee = employeRepository.findByEmail(email) ?: throw IllegalArgumentException("Employee not found")

            println("Found employee : ${employee.id}, email : ${employee.email}")

            val savedAttendance = attendanceService.addAttendance(dto, employee)
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAttendance)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error : ${ex.message}")
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error : ${ex.message}")
        }
    }

    @PutMapping("/update")
    fun updateAttendance(
        @RequestBody dto: UpdateAttendanceDTO,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        try {
            val authHeader = request.getHeader("Authorization") ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token")
            if (!authHeader.startsWith("Bearer ")) {
                println("token error")
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")
            }

            val token = authHeader.substring(7)

            val email = jwtService.extractUsername(token) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user")

            val employee = employeRepository.findByEmail(email) ?: throw IllegalArgumentException("Employee not found")

            println("Found employee : ${employee.id}, email : ${employee.email}")

            val attendanceId = dto.attendanceId
            val updatedAttendance = attendanceService.updateAttendance(attendanceId, dto, employee)

            if (updatedAttendance == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendance not found or unauthorized update")
            }

            return ResponseEntity.status(HttpStatus.OK).body(updatedAttendance)
        } catch (ex: IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error : ${ex.message}")
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error : ${ex.message}")
        }
    }
}