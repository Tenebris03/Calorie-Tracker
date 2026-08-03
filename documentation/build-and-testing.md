# Build and Testing

## Required Environment

The project documentation specifies:

- Android Studio Ladybug or newer
- JDK 21
- Gradle 9.4
- Android SDK with API 36 available

The Gradle wrapper is included in the repository. Use `./gradlew` rather than a globally installed Gradle version.

## Optional Gemini Configuration

The Gemini coach needs an API key. For a local build, create a `secrets.properties` file with:

```properties
GEMINI_API_KEY=your_key_here
```

The app can also accept a key from Settings through encrypted local storage.

## Useful Commands

```bash
./gradlew ktlintCheck
./gradlew detekt
./gradlew lint
./gradlew test
./gradlew assembleDebug
```

The continuous integration workflow runs:

```bash
./gradlew ktlintCheck detekt lint test assembleDebug
```

## Continuous Integration

GitHub Actions runs on pushes and pull requests targeting `main`. The workflow:

1. Checks out the repository.
2. Installs Temurin JDK 21.
3. Configures Gradle.
4. Accepts Android SDK licenses.
5. Creates `secrets.properties` only when a Gemini secret is available.
6. Runs linting, static analysis, unit tests, and a debug build.
7. Uploads reports as build artifacts.

## Existing Tests

The project currently includes:

- Architecture tests using Konsist
- Example JVM unit tests
- Room DAO instrumented tests
- Example instrumented Android tests

The previous heuristic detector tests were removed when the heuristic implementation was replaced with the TFLite classifier.

## Recommended AI Tests

The AI pipeline would benefit from these additional tests:

- Tokenizer special-token and padding behavior
- Tokenizer vocabulary ordering
- Missing model asset behavior
- Missing vocabulary behavior
- Model output probability conversion
- Threshold behavior at 0.48, 0.49, and 0.50
- Gemini JSON parsing
- Invalid Gemini response handling
- WorkManager retry behavior
- Notification behavior when `criticalAlert` is false or true

## Manual Demonstration Test

Use a physical device or emulator with network access:

1. Install a debug build.
2. Complete onboarding.
3. Add an ordinary food entry and confirm it appears in the log.
4. Add a food entry with a photo and verify Gemini returns an estimate.
5. Add a barcode product and verify Open Food Facts data appears.
6. Add a test entry likely to pass the local gatekeeper.
7. Confirm WorkManager creates the coach task.
8. Confirm Gemini returns a response.
9. Confirm a notification appears only for a critical response.

## Current Verification Status

The TFLite model and vocabulary are present in `core/data/src/main/assets/`. Static file and documentation checks have been performed. A full Gradle build must be run in Android Studio or a JDK 21 environment before claiming that the final model integration has been runtime-verified.
