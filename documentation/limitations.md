# Limitations and Future Improvements

## Model Limitations

The DistilBERT training dataset is small and manually created. The model may memorize food names instead of learning broad nutritional relationships.

The local model is a gatekeeper, not a nutritional expert. Its class labels are project-specific and should not be interpreted as medical recommendations.

The float16 model is approximately 128 MB. This is acceptable for a demonstration but large compared with a typical lightweight mobile model.

## Gemini Limitations

Gemini requires:

- Internet access
- A valid API key
- Available API quota

The model can return malformed JSON, incorrect food identification, inaccurate nutrition values, or unsuitable coaching. The application handles many failures by returning an error or silently retrying, but it cannot guarantee correct AI output.

## Vision Limitations

Photo recognition does not reliably know:

- Hidden ingredients
- Exact serving weight
- Cooking oil quantity
- Brand-specific nutrition
- Ingredients inside mixed dishes

Barcode data also depends on Open Food Facts coverage and accuracy.

## Context Limitations

Calendar and location context is optional. Without permissions, the coach receives reduced context. Weather is obtained from Open-Meteo and can fail due to network or location problems.

The current coach context does not represent a complete medical or nutritional profile. It should not be used to diagnose eating disorders, allergies, disease, or other health conditions.

## Product Limitations

- Single user and single device
- No accounts
- No cloud synchronization
- No backup and restore
- No formal privacy policy
- No medical validation
- No production-grade telemetry or observability
- Limited ViewModel and AI integration test coverage

## Technical Improvements

Possible future work includes:

- Expand and balance the model dataset.
- Add a held-out evaluation set instead of evaluating only on training data.
- Quantize or replace DistilBERT with a smaller mobile model.
- Add model versioning and asset integrity checks.
- Move AI work behind explicit interfaces for easier testing.
- Add dependency-injected fake Gemini and TFLite implementations.
- Add structured error states to the UI.
- Add rate limiting and a better coach cooldown policy.
- Improve JSON schema validation for Gemini responses.
- Add user confirmation before sending photos or sensitive context to cloud services.
- Add accessibility and larger-screen testing.
