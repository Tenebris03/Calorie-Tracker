# AI Toolchains

Helix contains several different AI or ML paths. They should be described separately because they run in different places and solve different problems.

## Toolchain Summary

| Toolchain | Runtime | Purpose |
| --- | --- | --- |
| Gemini Generative AI SDK | Cloud | Food photo nutrition estimation and final coaching |
| ML Kit Image Labeling | Device | Basic food/not-food image gate |
| ML Kit Barcode Scanning | Device | Barcode detection, not nutrition intelligence |
| DistilBERT + TensorFlow Lite | Device | Local coaching candidate filter |
| PyTorch + Transformers + ONNX + onnx2tf | Colab or computer | Train and export the DistilBERT model |

## Gemini Vision

`FoodVisionService` creates a Gemini request containing a bitmap and a structured prompt. The prompt requests a JSON object with:

- Food name
- Estimated weight in grams
- Calories per 100 grams
- Protein per 100 grams
- Fat per 100 grams
- Carbohydrates per 100 grams
- Fiber per 100 grams

The service removes common Markdown code fences and deserializes the response with Kotlin serialization. It retries server exceptions up to three attempts.

The response is an estimate. Image quality, food composition, lighting, hidden ingredients, and portion size can all reduce accuracy.

## ML Kit Image Labeling

`FoodGatekeeper` uses ML Kit's default image labeler. It accepts the image if any label contains the word `Food` with at least 0.4 confidence.

This component does not identify nutrition or calories. It only prevents obviously unrelated images from being sent to Gemini. If ML Kit fails, the current implementation allows the image through so the Gemini service can make the final attempt.

## ML Kit Barcode Scanning

ML Kit detects the barcode value. The application then uses the value to query Open Food Facts. The barcode model does not infer nutritional information itself.

## Local DistilBERT Gatekeeper

The local classifier is a two-class DistilBERT sequence classifier exported to TFLite. The current Android runtime requires the float32 TFLite export because the float16 export fails on the DistilBERT `GATHER` operation. Its classes are:

- Class 0: skip Gemini
- Class 1: worth evaluating with Gemini

The Android implementation:

1. Loads `food_problem_detector.tflite` from module assets.
2. Loads `vocab.txt` from module assets.
3. Tokenizes the same context format used during training.
4. Runs the input IDs and attention mask through the TFLite interpreter.
5. Applies softmax to the two output logits.
6. Treats a class-1 probability of at least 0.49 as a coaching candidate. This is a deliberately low threshold for the small school-demo model.

If either asset is missing or inference fails, the classifier returns zero risk. This prevents an unavailable local model from unexpectedly generating coach requests.

## Training Pipeline

The model is trained in `train_gatekeeper.ipynb` using:

1. Hugging Face Transformers for the DistilBERT model and tokenizer.
2. PyTorch for dataset loading, loss calculation, and optimization.
3. A frozen DistilBERT base with a trainable classification head.
4. ONNX export as an intermediate model format.
5. `onnx2tf` conversion into TFLite.
6. A vocabulary export for the Android WordPiece tokenizer.

The current model is a school-demo model. Its hand-written dataset is small, and its predictions should not be treated as health or medical advice.

## Gemini Coach

`InvisibleCoachWorker` is scheduled only after the local gatekeeper accepts an entry. The worker:

1. Reads the food log and remaining calories from WorkManager input.
2. Retrieves the stored Gemini API key.
3. Reads optional location, calendar, weather, and historical trend context.
4. Sends the combined prompt to Gemini.
5. Parses a `CoachResponse` JSON object.
6. Saves the response if it is critical.
7. Sends a notification and tactile alert for critical responses.

Gemini is the final decision-maker. The local DistilBERT model only controls whether Gemini should inspect the entry.

## Why Two Models Are Used

Using only Gemini would be simpler, but it would send every food entry to a cloud service and incur unnecessary latency or quota usage.

Using only DistilBERT would be fast and private, but it would struggle to reason about calendar context, weather, historical patterns, and nuanced user goals.

The two-stage design combines local filtering with cloud reasoning:

- DistilBERT: fast, local, inexpensive candidate filtering
- Gemini: flexible, contextual, cloud-based final reasoning

## Risks And Evaluation

The main AI evaluation risks are:

- The small dataset may cause memorization.
- Class labels reflect project assumptions rather than clinical standards.
- Nutrition estimates from images are uncertain.
- Gemini may produce invalid JSON or an unsuitable recommendation.
- The local model can be large for a mobile application.
- A high local probability does not guarantee that Gemini will produce a warning.
