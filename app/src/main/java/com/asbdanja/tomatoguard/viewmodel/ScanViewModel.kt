package com.asbdanja.tomatoguard.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.asbdanja.tomatoguard.data.HistoryRepository
import com.asbdanja.tomatoguard.ml.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class ScanState {
    data object Idle : ScanState()
    data object Loading : ScanState()
    data class Success(val result: PredictionResult, val bitmap: Bitmap) : ScanState()
    data class Error(val message: String) : ScanState()
}

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Initialize the DataStore Repository
    private val repository = HistoryRepository(application)

    private val _state = MutableStateFlow<ScanState>(ScanState.Idle)
    val state: StateFlow<ScanState> = _state.asStateFlow()

    // 2. Replace the old _history with a StateFlow from the Repository
    // This will now automatically load and update from DataStore
    val history: StateFlow<List<PredictionResult>> = repository.historyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private var classifier: TomatoGuardClassifier? = null
    private var context: Context? = null

    fun initialize(context: Context) {
        this.context = context.applicationContext
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (classifier == null) {
                        classifier = TomatoGuardClassifier(context)
                    }
                } catch (e: Exception) {
                    _state.value = ScanState.Error("Failed to initialize model: ${e.message}")
                }
            }
        }
    }

    fun analyzeImage(uri: Uri) {
        val ctx = context ?: run {
            Log.e("ScanViewModel", "Context is null! Initialize not called.")
            _state.value = ScanState.Error("App internal error: Context missing")
            return
        }

        viewModelScope.launch {
            _state.value = ScanState.Loading
            Log.d("ScanViewModel", "Starting analysis for URI: $uri")

            try {
                val bitmap = withContext(Dispatchers.IO) {
                    ctx.contentResolver.openInputStream(uri)?.use {
                        BitmapFactory.decodeStream(it)
                    }
                }

                if (bitmap == null) {
                    _state.value = ScanState.Error("Could not read image file")
                    return@launch
                }

                if (classifier == null) {
                    _state.value = ScanState.Error("AI Model not ready")
                    return@launch
                }

                val result = withContext(Dispatchers.IO) {
                    classifier?.predict(bitmap)
                }

                if (result != null) {
                    Log.d("ScanViewModel", "Analysis Success: ${result.label}")
                    _state.value = ScanState.Success(result, bitmap)

                    // 3. Save the result to DataStore permanently
                    repository.saveResult(result)

                } else {
                    _state.value = ScanState.Error("AI failed to process the leaf")
                }

            } catch (e: Exception) {
                Log.e("ScanViewModel", "Error during analysis", e)
                _state.value = ScanState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun reset() {
        _state.value = ScanState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        classifier?.close()
        classifier = null
        Log.d("ScanViewModel", "ViewModel cleared: ONNX resources released.")
    }
}