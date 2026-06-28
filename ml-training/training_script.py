# ── STEP 1: Mounti Google Drive
from google.colab import drive
drive.mount('/content/drive')

# ── STEP 2: Install kagglehub
!pip install kagglehub --quiet

# Fix: install missing onnx dependency
!pip install onnxscript --quiet

# ── STEP 3: Download Dataset
import os
import kagglehub

print(" Downloading dataset...")
dataset_path = kagglehub.dataset_download("kaustubhb999/tomatoleaf")
print(f" Dataset ready at: {dataset_path}")

# Known structure: <dataset_path>/tomato/train  and  /tomato/val
train_dir = os.path.join(dataset_path, "tomato", "train")
val_dir   = os.path.join(dataset_path, "tomato", "val")

# Safety check
assert os.path.exists(train_dir), f"train folder not found at: {train_dir}"
assert os.path.exists(val_dir),   f"val folder not found at:   {val_dir}"
print(f" Train: {train_dir}")
print(f" Val  : {val_dir}")

# ── STEP 4: Imports & Config ─────────────────────────────────
import torch
import torch.nn as nn
import torch.optim as optim
from torchvision import datasets, transforms, models
from torch.utils.data import DataLoader
import matplotlib.pyplot as plt

# Hyperparameters
BATCH_SIZE    = 32
EPOCHS        = 10
LEARNING_RATE = 0.001
DEVICE        = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Where to save results
SAVE_DIR = "/content/drive/MyDrive/TomatoGuard"
os.makedirs(SAVE_DIR, exist_ok=True)

print(f"\n  Batch size    : {BATCH_SIZE}")
print(f"  Epochs        : {EPOCHS}")
print(f"  Learning rate : {LEARNING_RATE}")
print(f"  Device        : {DEVICE}")
if DEVICE.type != "cuda":
    print("\n  WARNING: No GPU detected!")
    print("   Go to Runtime → Change runtime type → T4 GPU")

# ── STEP 5: Data Transforms ──────────────────────────────────
# ImageNet mean/std because MobileNetV2 was pretrained on ImageNet
MEAN = [0.485, 0.456, 0.406]
STD  = [0.229, 0.224, 0.225]

train_transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.RandomHorizontalFlip(),
    transforms.RandomVerticalFlip(),
    transforms.RandomRotation(15),
    transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
    transforms.ToTensor(),
    transforms.Normalize(MEAN, STD),
])

val_transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(MEAN, STD),
])

# ── STEP 6: Load Data ────────────────────────────────────────
train_dataset = datasets.ImageFolder(train_dir, transform=train_transform)
val_dataset   = datasets.ImageFolder(val_dir,   transform=val_transform)

train_loader = DataLoader(train_dataset, batch_size=BATCH_SIZE, shuffle=True,  num_workers=2, pin_memory=True)
val_loader   = DataLoader(val_dataset,   batch_size=BATCH_SIZE, shuffle=False, num_workers=2, pin_memory=True)

class_names  = train_dataset.classes
num_classes  = len(class_names)

print(f"\n Classes ({num_classes}):")
for i, name in enumerate(class_names):
    print(f" {i:2d}. {name}")
print(f"\n Train images : {len(train_dataset)}")
print(f" Val images   : {len(val_dataset)}")

# ── STEP 7: Build Model ──────────────────────────────────────
def build_model(num_classes):
    # Load MobileNetV2 pretrained on ImageNet
    model = models.mobilenet_v2(weights=models.MobileNet_V2_Weights.DEFAULT)

    # Freeze all layers except the last 20 parameters
    for param in list(model.parameters())[:-20]:
        param.requires_grad = False

    # Replace the classifier head for our number of classes
    in_features = model.classifier[1].in_features
    model.classifier[1] = nn.Sequential(
        nn.Linear(in_features, 512),
        nn.ReLU(),
        nn.Dropout(0.3),
        nn.Linear(512, num_classes)
    )
    return model

model = build_model(num_classes).to(DEVICE)

# Loss, optimizer, scheduler
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(
    filter(lambda p: p.requires_grad, model.parameters()),
    lr=LEARNING_RATE
)
scheduler = optim.lr_scheduler.StepLR(optimizer, step_size=5, gamma=0.1)

print(f"\n Model: MobileNetV2 → {num_classes} output classes")
trainable = sum(p.numel() for p in model.parameters() if p.requires_grad)
total     = sum(p.numel() for p in model.parameters())
print(f" Trainable params: {trainable:,} / {total:,}")

# ── STEP 8: Training ─────────────────────────────────────────
best_val_acc = 0.0
history = {'train_loss': [], 'train_acc': [], 'val_loss': [], 'val_acc': []}

print("\n" + "="*55)
print(" Training Started")
print("="*55)

for epoch in range(EPOCHS):
    print(f"\nEpoch {epoch+1}/{EPOCHS}")
    print("-" * 40)

    for phase, loader in [("train", train_loader), ("val", val_loader)]:
        model.train() if phase == "train" else model.eval()

        total_loss     = 0.0
        total_correct  = 0
        total_samples  = 0

        for images, labels in loader:
            images, labels = images.to(DEVICE), labels.to(DEVICE)
            optimizer.zero_grad()

            with torch.set_grad_enabled(phase == "train"):
                outputs = model(images)
                loss    = criterion(outputs, labels)
                _, preds = torch.max(outputs, 1)

                if phase == "train":
                    loss.backward()
                    optimizer.step()

            total_loss    += loss.item() * images.size(0)
            total_correct += (preds == labels).sum().item()
            total_samples += images.size(0)

        if phase == "train":
            scheduler.step()

        avg_loss = total_loss / total_samples
        avg_acc  = total_correct / total_samples

        history[f"{phase}_loss"].append(avg_loss)
        history[f"{phase}_acc"].append(avg_acc)

        print(f"  {phase.capitalize():<6} → Loss: {avg_loss:.4f}  Acc: {avg_acc:.4f}  ({avg_acc*100:.1f}%)")

        # Save best model
        if phase == "val" and avg_acc > best_val_acc:
            best_val_acc = avg_acc
            torch.save(model.state_dict(), os.path.join(SAVE_DIR, "best_model.pth"))
            print(f"  💾 Best model saved! ({best_val_acc*100:.2f}%)")

print("\n" + "="*55)
print(f" Training Complete — Best Val Acc: {best_val_acc*100:.2f}%")
print("="*55)

# ── STEP 9: Plot Training Curves ─────────────────────────────
epochs_x = range(1, EPOCHS + 1)

fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5))

ax1.plot(epochs_x, history['train_loss'], 'b-o', label='Train')
ax1.plot(epochs_x, history['val_loss'],   'r-o', label='Val')
ax1.set_title('Loss', fontsize=13, fontweight='bold')
ax1.set_xlabel('Epoch')
ax1.set_ylabel('Loss')
ax1.legend()
ax1.grid(True, alpha=0.3)

ax2.plot(epochs_x, history['train_acc'], 'b-o', label='Train')
ax2.plot(epochs_x, history['val_acc'],   'r-o', label='Val')
ax2.set_title('Accuracy', fontsize=13, fontweight='bold')
ax2.set_xlabel('Epoch')
ax2.set_ylabel('Accuracy')
ax2.set_ylim(0, 1)
ax2.legend()
ax2.grid(True, alpha=0.3)

fig.suptitle('TomatoGuard — Training History', fontsize=15, fontweight='bold')
plt.tight_layout()
plot_path = os.path.join(SAVE_DIR, "training_curves.png")
plt.savefig(plot_path, dpi=150, bbox_inches='tight')
plt.show()
print(f" Plot saved to Drive")

# ── STEP 10: Test on Sample Images ───────────────────────────
print("\n Running predictions on sample validation images...")

# Load the best saved model
model.load_state_dict(torch.load(os.path.join(SAVE_DIR, "best_model.pth"), map_location=DEVICE))
model.eval()

images, labels = next(iter(val_loader))
images = images.to(DEVICE)

with torch.no_grad():
    outputs = model(images)
    _, preds = torch.max(outputs, 1)

print(f"\n{'Predicted':<45} {'Actual':<45} {'Match'}")
print("-" * 95)
for i in range(min(10, len(preds))):
    pred_name   = class_names[preds[i].item()]
    actual_name = class_names[labels[i].item()]
    match       = "true" if pred_name == actual_name else "false"
    print(f"{pred_name:<45} {actual_name:<45} {match}")

# ── STEP 11: FIXED ONNX Export ──────────────────────────────────
print("\n📤 Exporting to ONNX with fixed settings...")

# Create a wrapper model to simplify the export
class MobileNetV2Wrapper(torch.nn.Module):
    def __init__(self, model):
        super().__init__()
        self.features = model.features
        self.classifier = model.classifier # This is our modified sequential classifier

    def forward(self, x):
        x = self.features(x)
        # MobileNetV2's features output needs to be flattened or pooled before the classifier
        # Global average pooling
        x = x.mean([2, 3])
        x = self.classifier(x)
        return x

# Use the wrapper
wrapper_model = MobileNetV2Wrapper(model)
wrapper_model.eval()

dummy = torch.randn(1, 3, 224, 224).to(DEVICE)
onnx_path = os.path.join(SAVE_DIR, "tomatoguard_fixed.onnx")

# Export with specific settings to avoid errors
torch.onnx.export(
    wrapper_model,
    dummy,
    onnx_path,
    export_params=True,
    opset_version=18,  # Use opset 18 to align with what onnxruntime is already using
    input_names=['input'],
    output_names=['output'],
    dynamic_axes={
        'input': {0: 'batch_size'},
        'output': {0: 'batch_size'}
    },
    do_constant_folding=True,
    verbose=False,
    training=torch.onnx.TrainingMode.EVAL,
    operator_export_type=torch.onnx.OperatorExportTypes.ONNX,
    keep_initializers_as_inputs=False
)

print(f"ONNX model saved to: {onnx_path}")

# Verify the exported model
import onnx
try:
    onnx_model = onnx.load(onnx_path)
    onnx.checker.check_model(onnx_model)
    print(" ONNX model verification passed!")
except Exception as e:
    print(f" Verification warning: {e}")
    print("Model was still saved but may have warnings.")

