package com.example.docenteapp.data

import com.example.docenteapp.domain.EvaluationRepository
import com.example.docenteapp.domain.models.Evaluation
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
}

class SettingsEvaluationRepository(private val settings: Settings) : EvaluationRepository {
    private val key = "evaluations_list"

    override fun saveEvaluation(evaluation: Evaluation) {
        try {
            val list = getAllEvaluations().toMutableList()
            list.add(evaluation)
            settings[key] = json.encodeToString(list)
        } catch (_: Exception) {}
    }

    override fun getEvaluationById(id: String): Evaluation? =
        getAllEvaluations().find { it.id == id }

    private fun getAllEvaluations(): List<Evaluation> = try {
        settings.getStringOrNull(key)?.let { json.decodeFromString<List<Evaluation>>(it) } ?: emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}
