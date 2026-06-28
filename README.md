# TomatoGuard 🍅

[![Python](https://img.shields.io/badge/Python-3.8+-blue?logo=python&logoColor=white)](https://www.python.org/)
[![PyTorch](https://img.shields.io/badge/PyTorch-2.0-red?logo=pytorch&logoColor=white)](https://pytorch.org/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-8.0+-green?logo=android&logoColor=white)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09-blue)](https://developer.android.com/jetpack/compose)
[![ONNX](https://img.shields.io/badge/ONNX-1.19-brightgreen)](https://onnx.ai/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Stars](https://img.shields.io/github/stars/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification?style=social)](https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification)

**AI-powered tomato leaf disease detection with 98.2% accuracy.**  
Runs offline on your Android phone. No server needed. No subscription fees.

[Features](#-features) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Model Info](#-model-info)

---

## 🌱 The Problem

Tomato crop losses from diseases: **30-50% annually** in developing countries

- 🚫 Farmers can't identify diseases quickly
- ⏱️ Manual diagnosis is time-consuming and subjective  
- 👨‍🌾 Expert agronomists aren't always accessible

## ✨ The Solution

**TomatoGuard** — AI that fits in your pocket.

- 📱 **Offline-first** — Works without internet
- 🚀 **Fast** — 50-100 ms inference time
- 🎯 **Accurate** — 98.2% on 10 disease classes
- 🆓 **Free & Open Source** — No subscription
- 🌍 **Global** — Works on tomatoes worldwide

---

## ✨ Features

- ✅ Real-time leaf disease detection
- ✅ 10 disease classifications + healthy leaves
- ✅ Scan history with confidence scores
- ✅ Beautiful Jetpack Compose UI
- ✅ On-device inference (no cloud uploads)
- ✅ 98.2% validation accuracy

---

## 🎬 Screenshots

| Home Screen | Scan Result | History |
|---|---|---|
| Pick a leaf photo | See disease prediction | View past scans |
| <img width="196" height="423" alt="Screenshot 2026-06-27 194721" src="https://github.com/user-attachments/assets/c228613a-d9ad-493c-b4ff-569cf591d3c6" /> |<img width="197" height="52" alt="Screenshot 2026-06-27 212548" src="https://github.com/user-attachments/assets/f5fcfce0-99b0-4b28-bc2a-cfeb20038a98" /> | <img width="196" height="425" alt="Screenshot 2026-06-27 212529" src="https://github.com/user-attachments/assets/6c3ffae0-5fb3-4e38-baf9-3d9a36dab70c" />
 |

---

## 📊 Model Performance

| Metric | Value |
|--------|-------|
| **Validation Accuracy** | 98.2% |
| **Inference Time** | 50-100 ms |
| **Model Size** | 3 MB (quantized) |
| **Training Dataset** | 10,000 images |
| **Disease Classes** | 10 |

### Supported Diseases

1. ✓ Bacterial spot
2. ✓ Early blight
3. ✓ Late blight
4. ✓ Leaf mold
5. ✓ Septoria leaf spot
6. ✓ Spider mites
7. ✓ Target spot
8. ✓ Yellow leaf curl virus
9. ✓ Mosaic virus
10. ✓ Healthy

---

## 🚀 Quick Start

### Prerequisites
- Android 8.0+ device or emulator
- ~45 MB storage

### Installation

1. **Clone the repository**
```bash
   git clone https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification.git
   cd TomatoGuard
```

2. **Download the model**
   - Get `model_android.onnx` from [Releases](https://github.com/Asbdanjah/Android-Based-CNN-Tomato-Leaf-Disease-Detection-Using-Image-Classification/releases)
   - Place in: `android-app/app/src/main/assets/`

3. **Build & Run**
```bash
   cd android-app
   ./gradlew build
   ./gradlew installDebug
```

4. **Open the app and scan a tomato leaf!**

---

## 📚 Documentation

- **[ML Model Details](docs/ML_MODEL.md)** — Model architecture, training process, performance metrics
- **[Contributing Guide](CONTRIBUTING.md)** — How to contribute
- **[Changelog](CHANGELOG.md)** — Version history
- **[Code of Conduct](CODE_OF_CONDUCT.md)** — Community guidelines

---

## 📁 Project Structure
