package com.asbdanja.tomatoguard.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.nio.FloatBuffer
import kotlin.math.exp
import androidx.core.graphics.scale
import kotlinx.serialization.Serializable

// ── Disease labels (must match your training class order) ────
val DISEASE_LABELS = listOf(
    "Bacterial spot",
    "Early blight",
    "Late blight",
    "Leaf mold",
    "Septoria leaf spot",
    "Spider mites",
    "Target spot",
    "Yellow leaf curl virus",
    "Mosaic virus",
    "Healthy"
)

@Serializable
data class PredictionResult(
    val label: String,
    val confidence: Float,
    val isHealthy: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class TomatoGuardClassifier(context: Context) {

    private val TAG = "TomatoGuardClassifier"
    private val ortEnv: OrtEnvironment = OrtEnvironment.getEnvironment()
    private val ortSession: OrtSession

    // ImageNet normalization constants
    private val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
    private val std  = floatArrayOf(0.229f, 0.224f, 0.225f)
    private val inputSize = 224

    init {
        try {
            Log.d(TAG, "Copying model files to internal storage...")
            // Move files from assets (virtual) to cache (physical)
            val modelPath = copyAssetToFile(context, "model_android.onnx")
            copyAssetToFile(context, "model_android.onnx.data") // Must be in same folder

            Log.d(TAG, "Loading ONNX session from: $modelPath")
            // Load using the FILE PATH string, not bytes
            ortSession = ortEnv.createSession(modelPath, OrtSession.SessionOptions())
            Log.d(TAG, "ONNX model loaded successfully!")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load ONNX model: ${e.message}")
            throw e
        }
    }

    private fun copyAssetToFile(context: Context, fileName: String): String {
        val file = java.io.File(context.cacheDir, fileName)
        // Only copy if it doesn't exist or you've updated the model
        context.assets.open(fileName).use { inputStream ->
            java.io.FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.absolutePath
    }

    fun predict(bitmap: Bitmap): PredictionResult? {
        return try {
            Log.d(TAG, "Starting prediction...")
            val resized = bitmap.scale(inputSize, inputSize)
            val inputTensor = bitmapToTensor(resized)

            val inputName = ortSession.inputNames.iterator().next()
            val inputs = mapOf(inputName to inputTensor)
            val output = ortSession.run(inputs)

            val rawScores = (output[0].value as Array<*>)[0] as FloatArray
            val probs = softmax(rawScores)

            val maxIdx = probs.indices.maxByOrNull { probs[it] } ?: 0
            val label = DISEASE_LABELS.getOrElse(maxIdx) { "Unknown" }
            val confidence = probs[maxIdx]

            Log.d(TAG, "Prediction completed: $label with ${confidence * 100}% confidence")

            inputTensor.close()
            output.close()

            PredictionResult(
                label = label,
                confidence = confidence,
                isHealthy = label == "Healthy"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error during prediction: ${e.message}")
            null
        }
    }

    // Convert Bitmap → [1, 3, 224, 224] float tensor with ImageNet normalization
    private fun bitmapToTensor(bitmap: Bitmap): OnnxTensor {
        val pixels = IntArray(inputSize * inputSize)
        bitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        val buffer = FloatBuffer.allocate(1 * 3 * inputSize * inputSize)

        // ONNX expects CHW layout: all R values, then all G, then all B
        for (c in 0..2) {
            for (pixel in pixels) {
                val channelValue = when (c) {
                    0 -> ((pixel shr 16) and 0xFF) / 255f  // R
                    1 -> ((pixel shr 8)  and 0xFF) / 255f  // G
                    else -> (pixel       and 0xFF) / 255f  // B
                }
                buffer.put((channelValue - mean[c]) / std[c])
            }
        }
        buffer.rewind()

        val shape = longArrayOf(1, 3, inputSize.toLong(), inputSize.toLong())
        return OnnxTensor.createTensor(ortEnv, buffer, shape)
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val maxVal = logits.max()
        val exps = logits.map { exp((it - maxVal).toDouble()).toFloat() }
        val sum = exps.sum()
        return exps.map { it / sum }.toFloatArray()
    }

    fun close() {
        try {
            ortSession.close()
            ortEnv.close()
            Log.d(TAG, "Classifier resources closed.")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing resources: ${e.message}")
        }
    }
}