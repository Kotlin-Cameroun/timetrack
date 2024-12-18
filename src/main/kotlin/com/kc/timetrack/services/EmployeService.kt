package com.kc.timetrack.services

import com.kc.timetrack.models.Employee
import com.kc.timetrack.repository.EmployeRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class EmployeService(
    private val employeRepository: EmployeRepository,
    private val passwordEncoder: BCryptPasswordEncoder
): UserDetailsService {

    fun createEmployee(name: String, email: String, rawPassword: String): Employee {
        if (employeRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("An employee with this email already exists.")
        }

        // Hach du password
        val hashedPassword = passwordEncoder.encode(rawPassword)

        // Création et sauvegarde
        val employee = Employee(
            id = null,
            name = name,
            email = email,
            password = hashedPassword
        )

        return employeRepository.save(employee)
    }


    override fun loadUserByUsername(username: String?): UserDetails {
        val employee = employeRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Employee with email $username not found")

        return User(
            employee.email,
            employee.password,
            emptyList()
        )
    }

    fun getCurrentEmployee(): Employee? {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return if(authentication != null && authentication.isAuthenticated) {
            val email = (authentication.principal as User).username

            employeRepository.findByEmail(email)
        }else {
            throw IllegalArgumentException("An employee with this email already exists.")
        }
    }
}

/**
 *
 * getCurrentEmployee() : Cette méthode utilise SecurityContextHolder pour récupérer l'utilisateur actuellement connecté.
 * Elle accède à l'email de l'utilisateur connecté à partir de l'objet User fourni par Spring Security.
 * Ensuite, elle utilise cet email pour récupérer l'entité Employee correspondante dans le repository.
 *
 * Récupération de l'employé : Si l'utilisateur est authentifié,
 * la méthode retourne l'employé correspondant à l'email de l'utilisateur actuellement connecté.
 * Si l'utilisateur n'est pas connecté, elle retourne null.
 *
 *
 *
 */