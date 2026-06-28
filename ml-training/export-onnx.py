import shutil
import os

files_to_zip = [
    "model_android.onnx",
    "model_android.onnx.data"
]


import zipfile
with zipfile.ZipFile("tomatoguard_model.zip", "w") as zf:
    for f in files_to_zip:
        if os.path.exists(os.path.join("/content/drive/MyDrive/TomatoGuard/", f)):
            zf.write(os.path.join("/content/drive/MyDrive/TomatoGuard/", f), arcname=f)
        else:
            print(f"Warning: {f} not found in /content/drive/MyDrive/TomatoGuard/")

print(" Zipped! Now download tomatoguard_model.zip")
