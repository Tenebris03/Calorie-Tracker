# DistilBERT Coach Gatekeeper

This document explains the local machine-learning gatekeeper used by Helix and the process for training and installing it.

## Purpose

The app uses two different models for two different jobs:

1. DistilBERT runs locally on the phone and decides whether a food entry is worth sending to the coach.
2. Gemini runs in the cloud and makes the final decision about whether the user should actually receive a warning.

The flow is:

```text
User logs food
        |
        v
Local DistilBERT classifier
        |
        | high enough risk probability
        v
Cloud Gemini coach
        |
        | criticalAlert == true
        v
Notification
```

This keeps normal food entries local and avoids making a Gemini request for every entry. The DistilBERT model is only a gatekeeper. It is not medical advice and it does not make the final health judgment.

## What The App Sends To The Local Model

The local model receives a text representation of `FoodContext`:

```text
Food: pizza. Calories: 800cal. Protein: 30g. Fat: 35g.
Carbohydrates: 90g. Fiber: 4g. Time: 21h.
Remaining calories: -300cal. Daily target: 2000cal. Goal: Lose.
```

The context includes:

- Food name
- Calories
- Protein
- Fat
- Carbohydrates
- Fiber
- Time of day
- Remaining daily calories
- Daily calorie target
- User goal

The classifier returns a probability. The app sends the entry to Gemini when the probability is at least `0.49`. This demo threshold is lower than the original target because the small training dataset is not well calibrated.

The final Gemini prompt also includes historical trends, weather, calendar context, the user's goal, macros, and calorie budget. Gemini is instructed not to warn about an occasional food if it fits the user's personal context.

## Project Implementation

The main implementation files are:

- `core/data/src/main/java/com/tenebris/health_tracker/data/service/FoodProblemDetector.kt`
  - Defines `FoodContext`, `FoodRisk`, and the detector interface.
- `core/data/src/main/java/com/tenebris/health_tracker/data/service/DistilBertFoodClassifier.kt`
  - Loads the TFLite model and performs local inference.
- `core/data/src/main/java/com/tenebris/health_tracker/data/service/DistilBertTokenizer.kt`
  - Small WordPiece tokenizer compatible with the exported DistilBERT vocabulary.
- `core/data/src/main/java/com/tenebris/health_tracker/core/data/di/CoreDataModule.kt`
  - Provides the classifier through Koin.
- `feature/dashboard/src/main/java/com/tenebris/health_tracker/ui/dashboard/DashboardViewModel.kt`
  - Builds the context after a food entry is added and triggers the cloud coach only when the local probability passes the threshold.
- `core/data/src/main/java/com/tenebris/health_tracker/data/service/CoachPromptBuilder.kt`
  - Defines the final Gemini decision rules.
- `train_gatekeeper.ipynb`
  - Trains the classifier and exports the TFLite model.

The old keyword-based `HeuristicFoodProblemDetector` was removed. If the model files are missing, the classifier returns zero risk and does not trigger coaching.

## Training The Model

Training is done outside the Android project, normally in Google Colab. Training is not performed on the phone.

Open `train_gatekeeper.ipynb` in Google Colab and run the cells in order.

The notebook performs these steps:

1. Installs PyTorch, Transformers, datasets, TensorFlow, ONNX, and `onnx2tf`.
2. Defines labeled food examples.
3. Converts each example into the same text format used by the Android app.
4. Loads `distilbert-base-uncased` with two output classes.
5. Freezes the base DistilBERT layers and trains only the classification head.
6. Exports the trained model to ONNX.
7. Converts the ONNX model to TFLite.
8. Exports the model vocabulary for the Android tokenizer.

The labels mean:

- `0`: safe enough to skip Gemini
- `1`: worth sending to Gemini for evaluation

These labels are only demo labels. They are not medical or nutritional recommendations.

## Training Data

The current notebook contains a small set of hand-written examples. It is sufficient to demonstrate the pipeline, but it is not enough for a reliable classifier.

The current examples can cause the model to memorize simple associations such as "pizza is risky". Better demo results come from adding varied examples involving:

- Different foods and portion sizes
- Healthy and unhealthy versions of similar meals
- Different calorie budgets
- Different user goals
- Different times of day
- Foods that should clearly be ignored
- Occasional treats that fit the user's remaining budget

For a stronger school demonstration, use approximately 100 varied examples. The examples should use the same context fields that the app sends at runtime.

## Exporting The Model In Colab

The ONNX-to-TFLite conversion command must use the `-ow` overwrite flag with current versions of `onnx2tf`:

```python
!onnx2tf -i model.onnx -o tflite_out -ow -qt per-tensor
```

The older `-onw` flag is ambiguous in newer versions and produces an error like:

```text
onnx2tf: error: ambiguous option: -onw
```

After conversion, `tflite_out` should contain files similar to:

```text
model_float16.tflite
model_float32.tflite
```

The float32 model is currently required by the Android TensorFlow Lite runtime. The float16 export can fail during interpreter initialization with `FLOAT16 is not supported by gather`. The float32 model is approximately 255 MB, so it is large for a mobile app but usable for a school project.

## Exporting The Vocabulary

Depending on the Transformers version, `tokenizer.save_pretrained()` may create `tokenizer.json` instead of `vocab.txt`.

The Android tokenizer needs `vocab.txt`, with one token per line in token ID order. Generate it in Colab with:

```python
vocab = tokenizer.get_vocab()

with open("vocab.txt", "w") as file:
    for token, token_id in sorted(vocab.items(), key=lambda item: item[1]):
        file.write(token + "\n")
```

Verify the file:

```python
!wc -l vocab.txt
!head -5 vocab.txt
```

For `distilbert-base-uncased`, the vocabulary should contain roughly 30,000 lines and start with special tokens such as `[PAD]`.

## Installing The Files In Android

Copy the smaller model and vocabulary into the data module's assets directory:

```python
!cp tflite_out/model_float32.tflite food_problem_detector.tflite
```

Download both files from Colab:

```python
from google.colab import files

files.download("food_problem_detector.tflite")
files.download("vocab.txt")
```

Place the downloaded files here in the project:

```text
core/data/src/main/assets/food_problem_detector.tflite
core/data/src/main/assets/vocab.txt
```

The files are intentionally not checked into the source tree by default because the model is large. The assets directory contains a `.gitkeep` file so the expected location exists.

The data module includes the TensorFlow Lite dependency and disables compression for `.tflite` files so Android can memory-map the model efficiently.

## Local Versus Cloud Execution

Training runs in Google Colab or another Python environment.

After the model is exported, DistilBERT runs locally on the Android device. The food context is evaluated locally first.

Gemini remains cloud-based. Only entries that pass the local gatekeeper are sent to Gemini, along with the context required for the final coaching decision.

## Troubleshooting

### `food_problem_detector.tflite` cannot be downloaded

This means the conversion cell did not create the output file. Check the conversion output and verify that `tflite_out/model_float32.tflite` exists.

### `tflite_out` does not exist

The ONNX-to-TFLite conversion did not run or failed. Re-run:

```python
!pip install -q onnx onnx2tf tf-keras
!onnx2tf -i model.onnx -o tflite_out -ow -qt per-tensor
```

### `model_assets/vocab.txt` does not exist

The tokenizer exported only `tokenizer.json`. Generate `vocab.txt` manually using the vocabulary conversion code above.

### The app builds but never triggers coaching

Check that both asset files exist in:

```text
core/data/src/main/assets/
```

If either file is missing, the classifier safely returns zero risk. This is intentional and prevents the app from silently falling back to the removed heuristic.

## Demo Limitations

- The training dataset is small and manually labeled.
- The classifier is not clinically validated.
- The model file is large for a phone application.
- Gemini requires network access and a valid API key.
- The local model only decides whether Gemini should inspect an entry; Gemini makes the final warning decision.
