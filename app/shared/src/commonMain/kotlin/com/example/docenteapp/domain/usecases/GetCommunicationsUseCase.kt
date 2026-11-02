package com.example.docenteapp.domain.usecases

import com.example.docenteapp.domain.CommunicationRepository
import com.example.docenteapp.domain.UserRepository
import com.example.docenteapp.domain.models.Communication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow

class GetCommunicationsUseCase(
    private val communicationRepository: CommunicationRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<List<Communication>> {
        val currentUser = userRepository.getSession() ?: return emptyFlow()
        return combine(
            communicationRepository.getCommunicationsForUser(currentUser.id),
            userRepository.getAllUsersFlow(),
        ) { comms, users ->
            val userMap = users.associateBy { it.id }
            comms.map { comm ->
                val sender = userMap[comm.senderId]
                val senderName = when {
                    sender != null -> "${sender.firstName} ${sender.lastName}".trim()
                    comm.senderId == currentUser.id -> "${currentUser.firstName} ${currentUser.lastName}".trim()
                    else -> "Usuario ${comm.senderId}"
                }
                comm.copy(senderName = senderName)
            }
        }
    }
}
