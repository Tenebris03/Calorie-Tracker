package com.tenebris.health_tracker.data.service

import android.content.Context
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class DistilBertFoodClassifier(
    private val context: Context,
) : FoodProblemDetector {
    private val interpreter: Interpreter? by lazy {
        try {
            Interpreter(loadModel())
        } catch (error: Exception) {
            Log.w(TAG, "Local coach model is not installed yet", error)
            null
        }
    }

    private val tokenizer: DistilBertTokenizer? by lazy {
        try {
            DistilBertTokenizer(context.assets.open(VOCAB_ASSET).bufferedReader().use { it.readLines() })
        } catch (error: Exception) {
            Log.w(TAG, "Local coach vocabulary is not installed yet", error)
            null
        }
    }

    override suspend fun evaluate(context: FoodContext): FoodRisk {
        val model = interpreter ?: return FoodRisk(0f)
        val tokenizer = tokenizer ?: return FoodRisk(0f)

        return try {
            val tokens = tokenizer.encode(context.toModelText(), model.getInputTensor(0).shape()[1])
            val inputs =
                arrayOf(
                    createInput(model.getInputTensor(0).dataType(), tokens),
                    createInput(model.getInputTensor(1).dataType(), tokenizer.attentionMask(tokens)),
                )
            val output = Array(1) { FloatArray(2) }
            model.runForMultipleInputsOutputs(inputs, mapOf(0 to output))
            val risk = FoodRisk(softmax(output[0])[1])
            Log.d(TAG, "riskProbability=${risk.probability}, shouldEvaluate=${risk.shouldEvaluate}")
            risk
        } catch (error: Exception) {
            Log.e(TAG, "Local coach model inference failed", error)
            FoodRisk(0f)
        }
    }

    private fun createInput(dataType: DataType, tokens: IntArray): Any =
        when (dataType) {
            DataType.INT64 -> Array(1) { tokens.map(Int::toLong).toLongArray() }
            DataType.FLOAT32 -> Array(1) { tokens.map(Int::toFloat).toFloatArray() }
            else -> Array(1) { tokens }
        }

    private fun loadModel(): MappedByteBuffer {
        return context.assets.openFd(MODEL_ASSET).use { descriptor ->
            FileInputStream(descriptor.fileDescriptor).use { input ->
                input.channel.map(FileChannel.MapMode.READ_ONLY, descriptor.startOffset, descriptor.declaredLength)
            }
        }
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val max = logits.maxOrNull() ?: 0f
        val values = logits.map { kotlin.math.exp((it - max).toDouble()) }
        val total = values.sum()
        return values.map { (it / total).toFloat() }.toFloatArray()
    }

    companion object {
        private const val TAG = "DistilBertFoodClassifier"
        private const val MODEL_ASSET = "food_problem_detector.tflite"
        private const val VOCAB_ASSET = "vocab.txt"
    }
}

private fun FoodContext.toModelText(): String =
    "Food: $foodName. Calories: ${calories}cal. Protein: ${protein}g. Fat: ${fat}g. " +
        "Carbohydrates: ${carbohydrates}g. Fiber: ${fiber}g. Time: ${hour}h. " +
        "Remaining calories: ${remainingCalories}cal. Daily target: ${calorieTarget}cal. Goal: $goal."
