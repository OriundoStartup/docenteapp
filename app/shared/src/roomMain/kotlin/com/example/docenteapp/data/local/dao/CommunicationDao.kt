package com.example.docenteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.docenteapp.data.local.entities.CommunicationEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Dao
interface CommunicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunication(communication: CommunicationEntity)

    @Query("SELECT * FROM communications WHERE senderId = :userId OR receiverId = :userId ORDER BY sentAt DESC")
    fun getCommunicationsForUser(userId: String): Flow<List<CommunicationEntity>>

    @Query("UPDATE communications SET isRead = 1 WHERE id = :communicationId")
    suspend fun markAsRead(communicationId: String)

    @Query("DELETE FROM communications")
    suspend fun clearAll()
}
