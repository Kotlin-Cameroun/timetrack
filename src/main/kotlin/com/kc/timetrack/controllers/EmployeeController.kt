package com.kc.timetrack.controllers

import com.kc.timetrack.config.JwtService
import com.kc.timetrack.dto.EmployeeRequest
import com.kc.timetrack.dto.EmployeeResponse
import com.kc.timetrack.dto.LoginRequest
import com.kc.timetrack.dto.LoginResponse
import com.kc.timetrack.models.Employee
import com.kc.timetrack.services.EmployeService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class EmployeeController(
    private val employeeService: EmployeService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    fun createEmployee(@RequestBody request: EmployeeRequest): ResponseEntity<EmployeeResponse> {
        val employee = employeeService.createEmployee(
            name = request.name,
            email = request.email,
            rawPassword = request.password
        )

        val response = EmployeeResponse(
            id = employee.id!!,
            name = employee.name,
            email = employee.email
        )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        // Génération du token JWT
        val userDetails = authentication.principal as UserDetails
        val jwtToken = jwtService.generateToken(userDetails)

        return ResponseEntity.ok(LoginResponse(token = jwtToken))
    }

    @GetMapping("/current-employee")
    fun getCurrentEmployee(): Employee? {
        return employeeService.getCurrentEmployee()
    }
}