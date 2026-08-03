# Features

## Onboarding

The onboarding form collects:

- Gender
- Age
- Height
- Weight
- Activity level
- Goal: lose, maintain, or gain
- Calorie offset

On completion, the app calculates a base metabolic rate using the Mifflin-St Jeor equation. It stores the initial weight, a dated profile entry, and the calculated preferences.

## Dashboard

The dashboard provides:

- Date selection through a timeline
- Daily calorie gauge
- Protein, fat, carbohydrate, and fiber totals
- Grouped food entries by breakfast, lunch, dinner, and snack
- Recent-food shortcuts
- Manual food entry
- Food editing and deletion
- Barcode scanning
- Photo recognition
- Coach card display

The dashboard derives target calories from the selected date's weight and profile where available. This allows historical entries to use historical profile information rather than always using today's settings.

## Manual Food Logging

The user enters a food name and nutrition values, chooses a meal type, and saves the entry. The entry is persisted locally in Room. Existing entries can be opened for editing or swiped for deletion.

## Barcode Scanning

The barcode flow uses CameraX and ML Kit barcode scanning. Once a barcode is detected:

1. The app checks its local cached product database.
2. If no cache entry exists, it queries Open Food Facts.
3. The response is converted into a cached product.
4. The user can review the returned nutrition values before logging the food.

Open Food Facts is a data source, not an AI model.

## Food Photo Recognition

The photo flow uses the device camera and a two-stage process:

1. ML Kit image labeling checks whether the image appears to contain food.
2. Gemini receives the image and estimates the food name, portion size, calories, protein, fat, carbohydrates, and fiber.

The result is explicitly an estimate. The app does not have a reliable physical scale or computer-vision portion measurement system.

## Progress Tracking

The progress screen allows the user to:

- Add weight entries
- Edit existing weight entries
- Delete weight entries
- View a weight graph
- Compare progress with a target weight

The tracking module is separate from the dashboard but uses the same data layer and Room database.

## Settings

Settings allows the user to change:

- Goal
- Calorie offset
- Activity level
- Age
- Height
- Target weight
- Gemini API key

Settings also provides optional permission controls for location and calendar context.

## Contextual Coach

The coach can use:

- Current food entry
- Current calorie budget
- User goal
- Nutrition values
- Recent calorie and protein trends
- Calendar events near the current time
- Current weather when location is available

The coach is intentionally unsolicited. It is triggered by a background WorkManager task rather than requiring the user to open a separate chat screen.

## Developer Tools

Debug and release builds currently expose developer tools through `SHOW_DEV_TOOLS`. The developer section can seed mock food, weight, and profile data for demonstrations and testing.
