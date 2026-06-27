package com.asbdanja.tomatoguard.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asbdanja.tomatoguard.ml.PredictionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore("history_prefs")

class HistoryRepository(private val context: Context) {
    private val HISTORY_KEY = stringPreferencesKey("scan_history")

    val historyFlow: Flow<List<PredictionResult>> = context.dataStore.data.map { prefs ->
        val json = prefs[HISTORY_KEY] ?: "[]"
        Json.decodeFromString(json)
    }

    suspend fun saveResult(result: PredictionResult) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[HISTORY_KEY] ?: "[]"
            val currentList: MutableList<PredictionResult> = Json.decodeFromString(currentJson)
            currentList.add(0, result) // Add new at top
            prefs[HISTORY_KEY] = Json.encodeToString(currentList)
        }
    }
}