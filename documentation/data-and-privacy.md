# Data, APIs, and Privacy

## Local Data

Room stores the primary application data locally:

- Food entries
- Weight entries
- Profile history
- Cached barcode products

DataStore stores preferences and coach state. The Room database is created as `health_tracker_v3_release`.

## Network Data Sources

Helix can communicate with:

- Open Food Facts for barcode nutrition data
- Google Gemini for image recognition and coaching
- Open-Meteo for weather context

Calendar information is read through Android's Calendar Provider when the user grants permission. Location is used to request weather context when the user grants location permission.

## Gemini API Key

The key can come from:

- Build-time `GEMINI_API_KEY` secrets configuration
- A key entered by the user in Settings

User-entered keys are stored through `EncryptedStorageManager`. The key should never be committed to source control.

## Permissions

The application declares:

- Internet
- Camera
- Read calendar
- Post notifications
- Coarse location

Camera, calendar, location, and notification access are runtime-sensitive capabilities. The app presents location and calendar as optional context permissions rather than making them mandatory for basic tracking.

## What Leaves The Device

Depending on the feature used:

- Barcode values may be sent to Open Food Facts.
- Food photos may be sent to Gemini for analysis.
- Suspicious food entries and contextual information may be sent to Gemini for coaching.
- The DistilBERT gatekeeper and ML Kit image-labeling check run locally.

The application is not a fully offline product because its Gemini features require network access.

## Privacy Considerations

Calendar titles, food logs, nutrition data, and context may be sensitive. The report should clearly state that optional context is used to improve coaching but can expose personal information to external services when Gemini is called.

The application is a demonstration and does not currently provide:

- Account-level privacy controls
- Remote data deletion
- A formal privacy policy
- End-to-end encryption for every network request beyond provider transport security
- A server-side user isolation layer

## Backup Decision

Backup and restore was intentionally removed because this is a single-device school demonstration. The application should not be described as supporting cross-device backup or synchronization.

## Data Safety Notes

- Do not place `secrets.properties` under version control.
- Do not include real personal calendar or health data in screenshots or training examples.
- Use synthetic data for demonstrations and model training.
- Do not describe Gemini or DistilBERT output as medical advice.
