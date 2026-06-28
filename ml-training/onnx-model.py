#Test ONNX Model
!pip install onnxruntime --quiet

import onnxruntime as ort
import numpy as np
from PIL import Image
import torchvision.transforms as transforms
import os

# ONNX model path
onnx_path = "/content/drive/MyDrive/TomatoGuard/tomatoguard_fixed.onnx"

# Checking if file exists
if not os.path.exists(onnx_path):
    print(f" ONNX model not found at: {onnx_path}")
    print(" Available files:")
    SAVE_DIR = "/content/drive/MyDrive/TomatoGuard"
    for f in os.listdir(SAVE_DIR):
        print(f"  • {f}")
else:
    print(f" Loading ONNX model from: {onnx_path}")

    # Load the ONNX model
    session = ort.InferenceSession(onnx_path)
    print(f" ONNX Runtime session created!")
    print(f" Input name: {session.get_inputs()[0].name}")
    print(f" Output name: {session.get_outputs()[0].name}")

    # Class names
    class_names = [
        'Tomato___Bacterial_spot',
        'Tomato___Early_blight',
        'Tomato___Late_blight',
        'Tomato___Leaf_Mold',
        'Tomato___Septoria_leaf_spot',
        'Tomato___Spider_mites Two-spotted_spider_mite',
        'Tomato___Target_Spot',
        'Tomato___Tomato_Yellow_Leaf_Curl_Virus',
        'Tomato___Tomato_mosaic_virus',
        'Tomato___healthy'
    ]

    # Preprocess function
    def preprocess_image(image_path):
        transform = transforms.Compose([
            transforms.Resize((224, 224)),
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])
        image = Image.open(image_path).convert('RGB')
        tensor = transform(image)
        return tensor.numpy().astype(np.float32)

    # Test prediction function
    def predict_onnx(image_path):
        # Preprocess
        input_data = preprocess_image(image_path)
        input_data = np.expand_dims(input_data, axis=0)

        # Run inference
        inputs = {session.get_inputs()[0].name: input_data}
        outputs = session.run(None, inputs)

        # Get prediction
        predictions = outputs[0][0]
        predicted_class = np.argmax(predictions)
        confidence = np.max(predictions) * 100

        return class_names[predicted_class], confidence

    print("\n ONNX model ready for predictions!")
    print("\n To test, upload an image using the cell below:")
