package com.tenebris.health_tracker.data.service

interface FoodProblemDetector {
    suspend fun evaluate(context: FoodContext): FoodRisk
}

data class FoodContext(
    val foodName: String,
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbohydrates: Int,
    val fiber: Int,
    val hour: Int,
    val remainingCalories: Int,
    val calorieTarget: Int,
    val goal: String,
)

data class FoodRisk(
    val probability: Float,
) {
    val shouldEvaluate: Boolean
        // The small school-demo model is not calibrated enough for a high cutoff.
        get() = probability >= 0.49f
}
