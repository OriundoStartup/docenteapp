package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.CommunicationDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.domain.CommunicationRepository
import com.example.docenteapp.domain.models.Communication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCommunicationRepository(private val communicationDao: CommunicationDao) : CommunicationRepository {
    override suspend fun saveCommunication(communication: Communication) {
        communicationDao.insertCommunication(communication.toEntity())
    }

    override fun getCommunicationsForUser(userId: String): Flow<List<Communication>> {
        return communicationDao.getCommunicationsForUser(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun markAsRead(communicationId: String) {
        communicationDao.markAsRead(communicationId)
    }
}
