package com.example.docenteapp.domain.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Instant

// Serializer for kotlin.time.Instant
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}

// Serializer for kotlinx.datetime.LocalDate
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())
}

enum class UserRole {
    PROFESSOR, PARENT
}

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant = Clock.System.now(),
    val professorProfile: Professor? = null,
    val parentProfile: Parent? = null,
)

@Serializable
data class Professor(
    val userId: String,
    val specialty: String,
    val bio: String? = null,
)

@Serializable
data class Parent(
    val userId: String,
    val phone: String,
    val address: String? = null,
)

@Serializable
data class College(
    val id: String,
    val name: String,
    val address: String,
    val identifier: String,
)

@Serializable
data class Course(
    val id: String,
    val name: String,
    val professorId: String,
    val collegeId: String,
    val accessCode: String,
    val period: String,
)

@Serializable
data class Student(
    val id: String,
    val firstName: String,
    val lastName: String,
    val rut: String,
    val courseId: String,
    val parentId: String,
)

enum class AttendanceStatus {
    PRESENT, ABSENT, LATE, JUSTIFIED
}

@Serializable
data class Attendance(
    val id: String,
    val studentId: String,
    val courseId: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val status: AttendanceStatus,
)

@Serializable
data class Evaluation(
    val id: String,
    val courseId: String,
    val description: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
)

@Serializable
data class Grade(
    val id: String,
    val studentId: String,
    val evaluationId: String,
    val value: Float,
)

@Serializable
data class GradeWithEvaluation(
    val grade: Grade,
    val evaluation: Evaluation,
)

@Serializable
data class Communication(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val title: String,
    val content: String,
    val isRead: Boolean = false,
    @Serializable(with = InstantSerializer::class)
    val sentAt: Instant,
    val senderName: String? = null,
)

enum class CertificateType {
    ENROLLMENT, GRADES_REPORT, ATTENDANCE_SUMMARY
}

@Serializable
data class Certificate(
    val id: String,
    val studentId: String,
    val studentName: String,
    val type: CertificateType,
    val issueDate: String,
    val content: String,
    val verificationCode: String,
)

@Serializable
data class RecentActivity(
    val title: String,
    val description: String,
    val type: String, // "GRADE", "ATTENDANCE", "COMMUNICATION"
    val date: String,
)
