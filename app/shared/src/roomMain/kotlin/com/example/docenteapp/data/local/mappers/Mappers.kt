@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.data.local.mappers

import com.example.docenteapp.data.local.entities.*
import com.example.docenteapp.domain.models.*
import kotlin.time.ExperimentalTime

fun UserEntity.toDomain() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    role = role,
    createdAt = createdAt
)

fun User.toEntity() = UserEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    role = role,
    createdAt = createdAt
)

fun CourseEntity.toDomain() = Course(
    id = id,
    name = name,
    professorId = professorId ?: "",
    collegeId = collegeId,
    accessCode = accessCode,
    period = period
)

fun Course.toEntity() = CourseEntity(
    id = id,
    name = name,
    professorId = professorId.ifEmpty { null },
    collegeId = collegeId,
    accessCode = accessCode,
    period = period
)

fun StudentEntity.toDomain() = Student(
    id = id,
    firstName = firstName,
    lastName = lastName,
    rut = rut,
    courseId = courseId,
    parentId = parentId ?: ""
)

fun Student.toEntity() = StudentEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    rut = rut,
    courseId = courseId,
    parentId = parentId.ifEmpty { null }
)

fun AttendanceEntity.toDomain() = Attendance(
    id = id,
    studentId = studentId,
    courseId = courseId,
    date = date,
    status = status
)

fun Attendance.toEntity() = AttendanceEntity(
    id = id,
    studentId = studentId,
    courseId = courseId,
    date = date,
    status = status
)

fun GradeEntity.toDomain() = Grade(
    id = id,
    studentId = studentId,
    evaluationId = evaluationId,
    value = value
)

fun Grade.toEntity() = GradeEntity(
    id = id,
    studentId = studentId,
    evaluationId = evaluationId,
    value = value
)

fun CommunicationEntity.toDomain() = Communication(
    id = id,
    senderId = senderId,
    receiverId = receiverId,
    title = title,
    content = content,
    isRead = isRead,
    sentAt = sentAt
)

fun Communication.toEntity() = CommunicationEntity(
    id = id,
    senderId = senderId,
    receiverId = receiverId,
    title = title,
    content = content,
    isRead = isRead,
    sentAt = sentAt
)

fun EvaluationEntity.toDomain() = Evaluation(
    id = id,
    courseId = courseId,
    description = description,
    date = date
)

fun Evaluation.toEntity() = EvaluationEntity(
    id = id,
    courseId = courseId,
    description = description,
    date = date
)

fun CollegeEntity.toDomain() = College(
    id = id,
    name = name,
    address = address,
    identifier = identifier
)

fun College.toEntity() = CollegeEntity(
    id = id,
    name = name,
    address = address,
    identifier = identifier
)
