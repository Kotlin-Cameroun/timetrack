package com.kc.timetrack.models

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "attendances")
data class Attendance (
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    val id: UUID?,

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,

    @Column(unique = true)
    val attendanceDate: LocalDate,

    @Column(nullable = true)
    val arrivalTime: LocalTime?,

    @Column(nullable = true)
    val breakStartTime: LocalTime?,

    @Column(nullable = true)
    val breakEndTime: LocalTime?,

    @Column(nullable = true)
    val departureTime: LocalTime?
)

/**
 * L'utilisation de data class offre plusieurs avantages, comme la génération automatique des méthodes toString(), equals(), hashCode(),
 */