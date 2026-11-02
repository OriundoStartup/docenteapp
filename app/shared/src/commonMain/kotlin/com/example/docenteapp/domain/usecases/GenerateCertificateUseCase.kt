@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.models.Certificate
import com.example.docenteapp.domain.models.CertificateType
import com.example.docenteapp.domain.models.Student
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.number
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GenerateCertificateUseCase {
    operator fun invoke(student: Student, type: CertificateType): Certificate {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val dateStr = "${now.day}/${now.month.number}/${now.year}"
        
        val content = when (type) {
            CertificateType.ENROLLMENT -> "Certificado de Alumno Regular para ${student.firstName}"
            CertificateType.GRADES_REPORT -> "Informe de Notas"
            CertificateType.ATTENDANCE_SUMMARY -> "Resumen de Asistencia"
        }

        val certIdPart = Random.nextLong(1000000000L, 9999999999L)
        val codePart1 = Random.nextInt(100000, 999999)
        val codePart2 = Random.nextInt(100000, 999999)

        return Certificate(
            id = "cert_$certIdPart",
            studentId = student.id,
            studentName = "${student.firstName} ${student.lastName}",
            type = type,
            issueDate = dateStr,
            content = content,
            verificationCode = "VERIFY-$codePart1-$codePart2"
        )
    }
}
