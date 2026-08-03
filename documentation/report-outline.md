# Suggested School Report Outline

## 1. Introduction

Explain the motivation for a health tracking application and why ordinary food logging can be improved with contextual coaching.

## 2. Problem Definition

Describe the problem the application addresses, the target user, and the boundaries of the project. State clearly that the project is a demonstration and not a medical product.

## 3. Requirements

Separate functional and non-functional requirements.

Functional requirements may include onboarding, food logging, barcode lookup, image recognition, weight tracking, settings, and coaching.

Non-functional requirements may include local persistence, responsive UI, modularity, privacy awareness, and acceptable mobile performance.

## 4. Technology Selection

Explain why the project uses:

- Kotlin and Jetpack Compose
- Room and DataStore
- Koin
- CameraX and ML Kit
- Open Food Facts
- Gemini
- DistilBERT and TensorFlow Lite
- WorkManager

For each technology, state what problem it solves and whether it runs locally or remotely.

## 5. System Architecture

Use the module diagram from `architecture.md`. Explain the application shell, feature modules, core modules, repositories, services, and ViewModels.

## 6. User Experience

Describe the onboarding, dashboard, food entry, progress, settings, and notification journeys. Include screenshots or annotated diagrams if available.

## 7. AI Design

Explain the two-stage AI approach:

1. Local DistilBERT filters candidate food entries.
2. Cloud Gemini makes the final context-aware decision.

Also describe the separate Gemini vision flow and ML Kit food-image check.

## 8. Model Training

Document the dataset format, labels, frozen DistilBERT layers, training environment, ONNX export, TFLite conversion, vocabulary export, and model installation.

Be transparent about the small demo dataset and its limitations.

## 9. Data and Privacy

Explain what is stored locally, what is sent to external services, which permissions are requested, and how API keys are handled.

## 10. Testing and Evaluation

Describe unit tests, instrumented tests, architecture tests, linting, static analysis, CI, and manual AI demonstrations.

Evaluate both technical behavior and model behavior. For the model, discuss false positives, false negatives, and the risk of memorizing food names.

## 11. Limitations

Use `limitations.md` to discuss model accuracy, cloud dependency, photo estimates, dataset size, privacy boundaries, and missing production features.

## 12. Conclusion and Future Work

Summarize what the project demonstrates: modular Android development, local ML inference, cloud generative AI, contextual data integration, and background notifications.

Suggest improvements such as a larger dataset, better evaluation, smaller model, stronger privacy controls, and broader testing.

## Suggested Figures

- Module dependency diagram
- Food photo recognition sequence diagram
- Coaching sequence diagram
- Screenshot of onboarding
- Screenshot of dashboard and macro totals
- Screenshot of progress graph
- Screenshot of settings and API key configuration
- Screenshot of coach notification
- Training and deployment pipeline diagram

## Suggested Tables

- Functional requirements and implementation status
- Technology and responsibility mapping
- Local versus cloud processing
- Test cases and results
- Risks and mitigations
