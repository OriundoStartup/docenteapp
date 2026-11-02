@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CommunicationRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Communication
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SendCommunicationUseCase(
    private val communicationRepository: CommunicationRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(title: String, content: String, receiverId: String): Result<Communication> {
        val user = userRepository.getSession() ?: return Result.failure(Exception("No hay sesión"))
        if (title.isEmpty() || content.isEmpty()) return Result.failure(Exception("Mensaje vacío"))
        
        val comm = Communication(
            id = Random.nextInt(1000, 9999).toString(),
            senderId = user.id, // Corrected: use user.id
            receiverId = receiverId,
            title = title,
            content = content,
            sentAt = Clock.System.now(),
            senderName = "${user.firstName} ${user.lastName}".trim(),
        )
        communicationRepository.saveCommunication(comm)
        return Result.success(comm)
    }
}
