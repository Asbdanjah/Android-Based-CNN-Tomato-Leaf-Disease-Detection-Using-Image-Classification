# TOMATOGUARD ONNX INFERENCE (FINAL) 
!pip install onnxruntime --quiet

from google.colab import files, drive
import numpy as np
from PIL import Image
import torchvision.transforms as transforms
import onnxruntime as ort
import matplotlib.pyplot as plt
import os

# Mounting to Drive
drive.mount('/content/drive')

# Load model
onnx_path = "/content/drive/MyDrive/TomatoGuard/tomatoguard_fixed.onnx"
session = ort.InferenceSession(onnx_path)

class_names = [
    'Bacterial_spot', 'Early_blight', 'Late_blight', 'Leaf_Mold',
    'Septoria_leaf_spot', 'Spider_mites', 'Target_Spot',
    'Yellow_Leaf_Curl', 'Mosaic_virus', 'Healthy'
]

def softmax(x):
    e_x = np.exp(x - np.max(x))
    return e_x / e_x.sum()

def predict_tomato(image_path, show_plot=True):
    """Predict tomato leaf disease from image"""

    # Preprocess
    transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ])

    image = Image.open(image_path).convert('RGB')
    tensor = transform(image).numpy().astype(np.float32)
    input_data = np.expand_dims(tensor, axis=0)

    # Run inference
    inputs = {session.get_inputs()[0].name: input_data}
    outputs = session.run(None, inputs)
    probabilities = softmax(outputs[0][0])

    # Get predictions
    top_idx = np.argmax(probabilities)
    confidence = probabilities[top_idx] * 100
    top3 = np.argsort(probabilities)[-3:][::-1]

    # Show results
    if show_plot:
        plt.figure(figsize=(6, 6))
        plt.imshow(image)
        plt.axis('off')
        plt.title(f" {class_names[top_idx]}\n{confidence:.2f}% confident")
        plt.show()

    results = {
        'prediction': class_names[top_idx],
        'confidence': confidence,
        'top_3': [(class_names[idx], probabilities[idx] * 100) for idx in top3],
        'is_healthy': 'healthy' in class_names[top_idx].lower()
    }

    return results

# Test
print(" Upload a tomato leaf image:")
uploaded = files.upload()

for filename in uploaded.keys():
    print(f"\n{'='*50}")
    print(f" Analyzing: {filename}")
    print('='*50)

    result = predict_tomato(filename)

    print(f"\n Prediction: {result['prediction']}")
    print(f" Confidence: {result['confidence']:.2f}%")

    if result['is_healthy']:
        print(" Status: HEALTHY - No disease detected!")
    else:
        print(f" Status: DISEASE DETECTED - {result['prediction']}")

    print("\n Top 3 Predictions:")
    for i, (name, conf) in enumerate(result['top_3'], 1):
        bar = '' * int(conf / 2) + '' * (50 - int(conf / 2))
        print(f"  {i}. {name}")
        print(f"     {bar} {conf:.2f}%")
