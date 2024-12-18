package com.kc.timetrack.repository

import com.kc.timetrack.models.Employee
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmployeRepository: JpaRepository<Employee, UUID> {
    fun findByEmail(email: String?): Employee?
}