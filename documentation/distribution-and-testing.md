# Distribution and Testing Without a Physical Android Phone

## Recommended Delivery Package

Provide the professor with:

1. `app-debug.apk`
2. A SHA-256 checksum
3. A short installation guide
4. The source-code ZIP
5. A note explaining that the Gemini API key must be entered by the tester

The APK already contains the local DistilBERT model and tokenizer. The tester does not need to install Python, TensorFlow, or the training environment.

## Build The APK

From the project root:

```bash
./gradlew assembleDebug --no-configuration-cache
```

The APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

The APK is large because it includes the float32 DistilBERT model. The current demonstration APK is approximately 575 MB.

## Create A Checksum

```bash
sha256sum app/build/outputs/apk/debug/app-debug.apk
```

Include the resulting checksum in the delivery instructions. This allows the professor to verify that the downloaded APK was not corrupted.

## Where To Upload It

Possible download locations include:

- The course notebook's file area
- Google Drive or OneDrive
- A private GitHub Release
- The university's file-sharing system

Do not upload `secrets.properties` or a personal Gemini API key.

## Option A: Android Studio Emulator

This is the most reliable option when no physical Android phone is available.

### Requirements

- Android Studio
- Hardware virtualization enabled in BIOS/UEFI
- Approximately 8 GB of free disk space for the emulator and SDK
- Internet access for Gemini and Open Food Facts features

### Setup

1. Install Android Studio.
2. Open **Device Manager**.
3. Create a virtual device such as a Pixel device.
4. Select an Android 16 / API 36 system image.
5. Start the emulator.
6. Drag `app-debug.apk` onto the running emulator, or use:

```bash
adb install -r app-debug.apk
```

7. Start Helix from the emulator app drawer.

### First Run

1. Complete onboarding.
2. Open Settings.
3. Enter a Gemini API key supplied by the tester.
4. Grant notification permission.
5. Grant camera permission if photo or barcode features will be tested.

The app works without a Gemini key for manual food logging, weight tracking, progress charts, and barcode lookup. Gemini features require a key and network access.

### Emulator Testing Limitations

- Camera behavior may differ from a physical phone.
- Barcode scanning may work less reliably with an emulator webcam.
- Notifications may be displayed inside the emulator instead of on a physical notification shade.
- The local DistilBERT model may take longer to initialize on a software-rendered emulator.

For a reliable demonstration, manual food logging and the coaching workflow are sufficient. Barcode and photo recognition can be demonstrated on a physical device or with a prepared video if the emulator camera is unreliable.

## Option B: Android Studio Run Configuration

Instead of distributing only the APK, the professor can receive the source ZIP:

1. Install Android Studio and JDK 21.
2. Open the Helix project.
3. Allow Gradle synchronization.
4. Create or select an API 36 emulator.
5. Select the `app` run configuration.
6. Press **Run**.

This option allows inspection of the source code, Logcat output, architecture, and AI pipeline.

## Option C: Physical Android Device

On a physical device:

1. Enable Developer Options.
2. Enable USB debugging.
3. Connect the device.
4. Approve the computer authorization prompt.
5. Install the APK:

```bash
adb install -r app-debug.apk
```

This provides the best camera, barcode, notification, and local-model experience.

## Gemini API Key Handling

The APK should not contain the developer's personal API key. The professor should enter a separate key in:

```text
Settings -> Invisible Coach -> Gemini API Key
```

The key is stored locally by the application. It is used for:

- Food photo recognition
- Invisible Coach requests

If no key is available, the professor can still test the non-Gemini features. A live Gemini demonstration can be performed by the student while screen sharing, or by providing a temporary test key according to the course rules.

## Suggested Professor Test Script

### Basic offline/local test

1. Install and open the app.
2. Complete onboarding.
3. Add an apple manually.
4. Confirm the food log and macro totals update.
5. Open Progress and add a weight entry.
6. Confirm the graph updates.

### Local AI and cloud coach test

1. Enter a Gemini API key.
2. Enable notifications.
3. Add a high-calorie pizza entry.
4. Wait for the background worker.
5. Confirm that the coach evaluates the entry and may show a notification.
6. Add a salad entry.
7. Confirm that Gemini can return `criticalAlert=false` and no warning is shown.

### Barcode test

1. Open Add Food.
2. Select the barcode scanner.
3. Grant camera permission.
4. Scan a product with an Open Food Facts entry.
5. Confirm that nutrition data is shown for review.

### Photo test

1. Open Add Food.
2. Select the camera/AI vision action.
3. Photograph a recognizable food.
4. Confirm that Gemini returns an estimate.
5. Review the values before saving.

## Evidence For The Report

The submission should include:

- Download link to the APK
- APK checksum
- Source ZIP link
- Emulator setup screenshot
- Successful installation screenshot
- Onboarding screenshot
- Dashboard screenshot
- Coach notification screenshot
- Logcat screenshot showing local model and WorkManager events
- A note stating which features require a Gemini API key

## Important Disclosure

The APK is a debug demonstration build. It is not distributed through the Google Play Store and may require the tester to approve installation from an external source. The application is a school project and must not be presented as medically validated software.
