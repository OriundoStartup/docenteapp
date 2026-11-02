package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.College

interface CollegeRepository {
    suspend fun saveCollege(college: College)
    suspend fun getCollegeById(id: String): College?
    suspend fun getCollegeByName(name: String): College?
}
