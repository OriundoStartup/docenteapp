package com.example.docenteapp.data.local

import com.example.docenteapp.data.local.dao.CollegeDao
import com.example.docenteapp.data.local.mappers.toDomain
import com.example.docenteapp.data.local.mappers.toEntity
import com.example.docenteapp.domain.CollegeRepository
import com.example.docenteapp.domain.models.College

class RoomCollegeRepository(
    private val collegeDao: CollegeDao
) : CollegeRepository {
    override suspend fun saveCollege(college: College) {
        collegeDao.insertCollege(college.toEntity())
    }

    override suspend fun getCollegeById(id: String): College? {
        return collegeDao.getCollegeById(id)?.toDomain()
    }

    override suspend fun getCollegeByName(name: String): College? {
        return collegeDao.getCollegeByName(name)?.toDomain()
    }
}
