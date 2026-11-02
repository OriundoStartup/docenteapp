package com.example.docenteapp.domain

import com.example.docenteapp.domain.models.Evaluation

interface EvaluationRepository {
    fun saveEvaluation(evaluation: Evaluation)
    fun getEvaluationById(id: String): Evaluation?
}
