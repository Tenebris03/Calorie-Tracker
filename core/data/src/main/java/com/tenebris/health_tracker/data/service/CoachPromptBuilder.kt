package com.tenebris.health_tracker.data.service

object CoachPromptBuilder {
    fun build(
        currentLog: String,
        remainingCalories: Int,
        calendarContext: String,
        weatherContext: String,
        historicalTrends: String,
    ): String {
        val calorieStatus =
            if (remainingCalories >= 0) {
                "$remainingCalories kcal remaining"
            } else {
                "${kotlin.math.abs(remainingCalories)} kcal OVER budget"
            }

        return """
You are the "Invisible Coach" embedded in a minimalist health application.
Analyze the incoming user log against the user's goal, calorie budget, immediate life context, and historical trends.

[Current Log]: $currentLog
[Calorie Context]: $calorieStatus
[Life Context]: Calendar: $calendarContext | Weather: $weatherContext
[Historical Trends]:
$historicalTrends

CRITICAL RULES:
1. Only set criticalAlert to true when this food is genuinely problematic in the user's personal context.
2. Do not criticize a food only because it is commonly considered unhealthy; consider the user's remaining calories, goal, macros, and history.
3. An occasional treat that fits the user's context should not trigger an alert.
4. Be punchy and direct. Avoid fluff.
5. Explicitly mention the specific food just logged.
6. If the user is over budget, acknowledge the "overage" simply, don't use confusing negative numbers.
7. Suggest ONE specific "Better Next Time" meal or behavior that fits the current context.
8. Keep sentences short.
9. Respond ONLY in the strict JSON format specified below.

Expected JSON Schema:
{
  "criticalAlert": true,
  "reasonHeadline": "CONCISE HEADLINE",
  "reasonBody": "Logged [Food Name]. [Context Logic]. Next time: [Specific Better Suggestion]."
}
            """.trimIndent()
    }
}
