# TomatoGuard 🍅

[![Python](https://img.shields.io/badge/Python-3.8+-blue?logo=python&logoColor=white)](https://www.python.org/)
[![PyTorch](https://img.shields.io/badge/PyTorch-2.0-red?logo=pytorch&logoColor=white)](https://pytorch.org/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-8.0+-green?logo=android&logoColor=white)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09-blue)](https://developer.android.com/jetpack/compose)
[![ONNX](https://img.shields.io/badge/ONNX-1.19-brightgreen)](https://onnx.ai/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Android-based CNN Tomato Leaf Disease Detection Using Image Classification with 98.2% accuracy.**  
Runs offline on your Android phone. No server needed. No subscription fees.

[Features](#-features) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Model Info](#-model-info)

---

##  The Problem

Tomato crop losses from diseases: **30-50% annually** in developing countries

-  Farmers can't identify diseases quickly
-  Manual diagnosis is time-consuming and subjective
-  Expert agronomists aren't always accessible

##  The Solution

**TomatoGuard** — AI that fits in your pocket.

-  **Offline-first** — Works without internet
-  **Fast** — 50-100 ms inference time
-  **Accurate** — 98.2% on 10 disease classes
-  **Free & Open Source** — No subscription
-  **Global** — Works on tomatoes worldwide

---

##  Features

- ✅ Real-time leaf disease detection
- ✅ 10 disease classifications + healthy leaves
- ✅ Scan history with confidence scores
- ✅ Beautiful Jetpack Compose UI
- ✅ On-device inference (no cloud uploads)
- ✅ 98.2% validation accuracy
- ✅ Works offline completely

---

##  Model Performance

| Metric | Value |
|--------|-------|
| **Validation Accuracy** | 98.2% |
| **Inference Time** | 50-100 ms |
| **Model Size** | 3 MB (quantized) |
| **Training Dataset** | 10,000 images |
| **Disease Classes** | 10 |
| **Trainable Parameters** | 1.7M |

### Supported Diseases

1. Bacterial spot
2. Early blight
3. Late blight
4. Leaf mold
5. Septoria leaf spot
6. Spider mites
7. Target spot
8. Yellow leaf curl virus
9. Mosaic virus
10. Healthy

---

##  Quick Start

### Prerequisites
- Android 7.0+ device or emulator
- ~45 MB storage space

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification.git
   cd TomatoGuard
   ```

2. **Download the model**
   - Download `model_android.onnx` from [Releases](https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification/releases)
   - Place in: `android-app/app/src/main/assets/model_android.onnx`

3. **Build & Run**
   ```bash
   cd android-app
   chmod +x gradlew
   ./gradlew build
   ./gradlew installDebug
   ```

4. **Open the app and scan a tomato leaf!**

---

## Documentation

- **[ML Model Details](docs/ML_MODEL.md)** — Model architecture, training, performance metrics
- **[Contributing Guide](CONTRIBUTING.md)** — How to contribute to the project
- **[Changelog](CHANGELOG.md)** — Version history and updates
- **[Code of Conduct](CODE_OF_CONDUCT.md)** — Community guidelines

---

## Project Structure

```
TomatoGuard/
├── android-app/              # Mobile application
│   ├── app/src/main/
│   │   ├── java/...          # Kotlin source code
│   │   ├── assets/           # ONNX model files
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── gradlew
├── ml-training/              # Machine learning code
│   ├── training_script.py    # PyTorch training script
│   ├── export_onnx.py        # ONNX export script
│   └── requirements.txt       # Python dependencies
├── docs/                     # Documentation
│   ├── ML_MODEL.md           # Technical model documentation
│   ├── figures/              # Screenshots and diagrams
│   └── README.md
├── .github/
│   └── workflows/            # GitHub Actions CI/CD
├── README.md
├── LICENSE
├── CHANGELOG.md
├── CONTRIBUTING.md
└── CODE_OF_CONDUCT.md
```

---

## How It Works

```
1. User selects leaf image from gallery
         ↓
2. Android app loads and preprocesses image
         ↓
3. Image resized to 224×224 pixels
         ↓
4. ONNX Runtime runs inference (~50ms)
         ↓
5. MobileNetV2 outputs disease probability
         ↓
6. App displays disease name + confidence score
         ↓
7. Result saved to scan history
```

---

## Tech Stack

**Mobile Development:**
- **UI Framework:** Jetpack Compose (declarative UI)
- **Language:** Kotlin 1.9+
- **State Management:** ViewModel + StateFlow
- **ML Inference:** ONNX Runtime Android
- **Min SDK:** Android 7.0 (API 24)

**Machine Learning:**
- **Training Framework:** PyTorch 2.0
- **Model Architecture:** MobileNetV2 (transfer learning)
- **Pretraining:** ImageNet weights
- **Optimization:** Post-training quantization
- **Export Format:** ONNX 1.14

**Dataset:**
- **Source:** PlantVillage (Kaggle)
- **Size:** 10,000 training + 1,000 validation images
- **Classes:** 10 (9 diseases + healthy)

---

## Model Architecture

**MobileNetV2** with custom classification head:

```
Input: 224×224 RGB image (normalized with ImageNet mean/std)
    ↓
MobileNetV2 Backbone (pretrained on ImageNet)
    ├─ Early layers: FROZEN (transfer learning)
    └─ Final layers: FINE-TUNED
    ↓
Custom Classifier Head:
    ├─ Linear(1280 → 512)
    ├─ ReLU activation
    ├─ Dropout(0.3)
    └─ Linear(512 → 10)
    ↓
Output: Softmax probabilities for 10 classes
    ↓
Prediction: argmax(probabilities) + confidence score
```

**Why MobileNetV2?**
- Lightweight architecture (3.5M parameters)
- Fast inference (~50-100 ms on mobile)
- High accuracy with transfer learning
- Small model size (11 MB → 3 MB quantized)
- Production-ready for mobile devices

---

## Training Details

**Hyperparameters:**
- Optimizer: Adam (learning rate 0.001)
- Loss Function: CrossEntropyLoss
- Batch Size: 32
- Epochs: 10
- Learning Rate Schedule: StepLR (decay ×0.1 at epoch 5)

**Data Augmentation:**
- Random horizontal flip
- Random vertical flip
- Random rotation (±15°)
- Color jitter (brightness, contrast, saturation)
- Resize to 224×224

**Results:**
- Best Validation Accuracy: **98.2%** (Epoch 8)
- Best Validation Loss: **0.0454**
- No overfitting detected (train ≈ val curves)

---

## Performance Highlights

- ✅ **98.2% accuracy** — Better than manual farmer diagnosis
- ✅ **50-100 ms inference** — Real-time feedback on device
- ✅ **3 MB model** — Fits on any Android phone
- ✅ **Offline inference** — No internet connection required
- ✅ **Privacy-first** — Images never leave your device
- ✅ **No cloud costs** — Completely free to use

---

##  Contributing

We welcome contributions! See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

### Ways to Help:

-  **Report bugs** — Found an issue? Open a GitHub issue
-  **Suggest features** — Have an idea? Share it!
-  **Improve documentation** — Help others understand the project
-  **Improve model accuracy** — Collect and label new data
-  **Enhance UI/UX** — Make the app more beautiful

---

##  Training the Model

Want to train on your own data or retrain from scratch?

1. **Install dependencies**
   ```bash
   cd ml-training
   pip install -r requirements.txt
   ```

2. **Prepare your dataset**
   - Download from [PlantVillage](https://www.kaggle.com/datasets/kaustubhb999/tomatoleaf)
   - Or use the auto-download in the training script

3. **Run training**
   ```bash
   python training_script.py
   ```

4. **Export to ONNX**
   ```bash
   python export_onnx.py
   ```

See [ML Model Documentation](docs/ML_MODEL.md) for detailed instructions.

---

## Limitations & Future Work

### Current Limitations
- Single leaf per image (designed for individual leaf photos)
- Lab-photographed dataset (may differ from real farm photos)
- Binary classification (disease/healthy only, no severity)

### Future Improvements
- Real-time camera scanning (live preview)
- Multi-leaf batch processing
- Disease severity estimation (mild/moderate/severe)
- Treatment recommendations in-app
- Ensemble models for 99%+ accuracy
- Multi-language support

---

## License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

You are free to use, modify, and distribute this software for personal or commercial purposes.

---

## Acknowledgments

- **PlantVillage Dataset** — For providing the training data
- **PyTorch & ONNX Communities** — For excellent tools
- **Android Jetpack Team** — For Compose framework
- **Open Source Contributors** — For inspiration and guidance

---

## Questions or Issues?

- 💬 Open a [GitHub Issue](https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification/issues) for bugs
- 💭 Start a [Discussions](https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification/discussions) for questions
- 📧 Contact for other inquiries

---

## Statistics

- **GitHub Stars:** ⭐ Growing!
- **Lines of Code:** 2,000+ (Kotlin) + 1,500+ (Python)
- **Model Accuracy:** 98.2%
- **Test Coverage:** Android unit tests included
- **Documentation:** Comprehensive

---

**Made with love for farmers and the open-source community**

Final Year Computer Science Project | University Submission | Open Source

---

**Last Updated:** June 28, 2026
**Version:** 1.0.0  
**Status:** Production Ready ✅
