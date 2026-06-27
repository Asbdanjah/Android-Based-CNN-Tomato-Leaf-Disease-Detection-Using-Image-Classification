package com.asbdanja.tomatoguard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asbdanja.tomatoguard.ml.PredictionResult
import com.asbdanja.tomatoguard.theme.ForestGreen
import com.asbdanja.tomatoguard.theme.LeafGreen
import com.asbdanja.tomatoguard.theme.LeafGreenLight
import com.asbdanja.tomatoguard.theme.TomatoRed
import com.asbdanja.tomatoguard.theme.TomatoRedDark

// ── Scan Card (idle state) ────────────────────────────────────
@Composable
fun ScanCard(onScan: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border   = androidx.compose.foundation.BorderStroke(1.5.dp, LeafGreenLight.copy(alpha = 0.4f))
    ) {
        Column(
            modifier            = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.CameraAlt, contentDescription = null,
                    tint = ForestGreen, modifier = Modifier.size(34.dp))
            }
            Text("Scan a tomato leaf",
                style      = MaterialTheme.typography.titleMedium,
                color      = ForestGreen,
                fontWeight = FontWeight.SemiBold)
            Text("Take or upload a photo to detect disease instantly",
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            Button(
                onClick  = onScan,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = TomatoRed)
            ) {
                Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Choose from gallery", fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ── Loading Card ──────────────────────────────────────────────
@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier            = Modifier.padding(40.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = ForestGreen, strokeWidth = 3.dp)
            Text("Analyzing leaf...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

// ── Result Card ───────────────────────────────────────────────
@Composable
fun ResultCard(
    result  : PredictionResult,
    bitmap  : android.graphics.Bitmap,
    onRescan: () -> Unit
) {
    val badgeColor = if (result.isHealthy) Color(0xFF43A047) else TomatoRed
    val bgColor    = if (result.isHealthy) Color(0xFFE8F5E9) else Color(0xFFFDECEA)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                bitmap             = bitmap.asImageBitmap(),
                contentDescription = "Scanned leaf",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(
                modifier            = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        if (result.isHealthy) "Healthy" else "Disease Detected",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color      = badgeColor
                    )
                }
                Text(result.label,
                    style      = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Confidence", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text("${(result.confidence * 100).toInt()}%",
                            style      = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color      = badgeColor)
                    }
                    LinearProgressIndicator(
                        progress   = { result.confidence },
                        modifier   = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp)),
                        color      = badgeColor,
                        trackColor = badgeColor.copy(alpha = 0.15f),
                    )
                }
                if (!result.isHealthy) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(10.dp),
                        color    = Color(0xFFFFF8E1)
                    ) {
                        Row(
                            modifier              = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Warning, contentDescription = null,
                                tint = Color(0xFFF57F17), modifier = Modifier.size(18.dp))
                            Text("Consult an agronomist or apply appropriate treatment.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF795548))
                        }
                    }
                }
                OutlinedButton(
                    onClick  = onRescan,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Scan another leaf")
                }
            }
        }
    }
}

// ── Error Card ────────────────────────────────────────────────
@Composable
fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color(0xFFFDECEA))
    ) {
        Column(
            modifier            = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Outlined.ErrorOutline, contentDescription = null,
                tint = TomatoRed, modifier = Modifier.size(36.dp))
            Text("Something went wrong", style = MaterialTheme.typography.titleMedium, color = TomatoRedDark)
            Text(message, style = MaterialTheme.typography.bodyMedium,
                color = TomatoRedDark.copy(alpha = 0.75f), textAlign = TextAlign.Center)
            TextButton(onClick = onRetry) { Text("Try again", color = TomatoRed) }
        }
    }
}

// ── Info Banner ───────────────────────────────────────────────
@Composable
fun InfoBannerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(
            containerColor = LeafGreen.copy(alpha = 0.05f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = LeafGreen.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LeafGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = "Pro Tip",
                    style = MaterialTheme.typography.labelLarge,
                    color = ForestGreen,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "For better accuracy, place the leaf on a plain background and ensure there is plenty of natural light.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ForestGreen.copy(alpha = 0.8f)
                )
            }
        }
    }
}

