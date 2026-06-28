#  DOWNLOAD MODEL FOR ANDROID 
from google.colab import files
import os

# Download the quantized model (recommended)
model_path = "/content/drive/MyDrive/TomatoGuard/model_quantized.pt"

if os.path.exists(model_path):
    size_mb = os.path.getsize(model_path) / (1024 * 1024)
    print(f" Downloading model_quantized.pt ({size_mb:.2f} MB)")
    files.download(model_path)
    print(" Download started!")
else:
    print(" Model not found")
