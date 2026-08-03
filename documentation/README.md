# Helix Documentation

This folder is the technical documentation set for the Helix school project. It is intended to provide the source material for a final written report, presentation, or demonstration.

## Documents

| Document | Purpose |
| --- | --- |
| [Project Overview](project-overview.md) | Problem, goals, users, and scope |
| [Architecture](architecture.md) | Modules, navigation, dependency injection, and data flow |
| [Features](features.md) | User-facing functionality and its implementation |
| [AI Toolchains](ai-toolchains.md) | Gemini, ML Kit, DistilBERT, and model training |
| [Data and Privacy](data-and-privacy.md) | Storage, APIs, permissions, secrets, and privacy considerations |
| [Build and Testing](build-and-testing.md) | Tool versions, build commands, CI, and tests |
| [Limitations](limitations.md) | Known gaps, risks, and demo constraints |
| [Report Outline](report-outline.md) | Suggested structure for the final school report |
| [Cookbook Entwurf](cookbook-entwurf.md) | German draft for the required PDF cookbook and live presentation |
| [Distribution and Testing](distribution-and-testing.md) | APK delivery and emulator testing without a physical phone |

## Current Project Snapshot

- Application type: Android health and calorie tracking app
- Package: `com.tenebris.health_tracker`
- UI: Jetpack Compose and Material 3
- Minimum Android version: API 34
- Compile and target SDK: API 36
- Main language: Kotlin
- Dependency injection: Koin
- Local database: Room
- Cloud generative model: Gemini
- Local text classifier: DistilBERT exported to TensorFlow Lite
- Runtime image and barcode ML: Google ML Kit

## Important Distinction

The project contains both AI and non-AI components.

AI/ML components include Gemini, ML Kit image labeling, ML Kit barcode recognition, and the local DistilBERT classifier.

Supporting components include Room, Retrofit, Open Food Facts, WorkManager, DataStore, weather APIs, calendar APIs, and deterministic calorie and trend calculations.

## Reading Order

For a quick understanding, read `project-overview.md`, `architecture.md`, and `ai-toolchains.md` first. For implementation and evaluation details, continue with `data-and-privacy.md`, `build-and-testing.md`, and `limitations.md`.
