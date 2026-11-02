package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.Communication
import kotlinx.coroutines.flow.Flow

interface CommunicationRepository {
    suspend fun saveCommunication(communication: Communication)
    fun getCommunicationsForUser(userId: String): Flow<List<Communication>>
    suspend fun markAsRead(communicationId: String)
}
