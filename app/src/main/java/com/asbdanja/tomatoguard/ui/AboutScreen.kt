package com.asbdanja.tomatoguard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Import for back arrow
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asbdanja.tomatoguard.theme.ForestGreen
import com.asbdanja.tomatoguard.theme.LeafGreen
import com.asbdanja.tomatoguard.theme.MintGreen
import com.asbdanja.tomatoguard.theme.TomatoGuardTheme

// ── About Screen ──────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("About") },
            // 2. Added navigationIcon for the back arrow
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ForestGreen,
                titleContentColor = Color.White
            )
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(LeafGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("TG", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("TomatoGuard", color = Color.White, style = MaterialTheme.typography.headlineMedium)
                        Text("v1.0.0", color = MintGreen, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            item {
                AboutInfoCard(
                    icon = Icons.Outlined.Psychology,
                    title = "AI model",
                    body = "MobileNetV2 trained on the PlantVillage dataset. Achieves 98.2% validation accuracy across 10 tomato disease classes."
                )
            }
            item {
                AboutInfoCard(
                    icon = Icons.Outlined.Category,
                    title = "Detectable diseases",
                    body = "Bacterial spot · Early blight · Late blight · Leaf mold · Septoria leaf spot · Spider mites · Target spot · Yellow leaf curl virus · Mosaic virus · Healthy"
                )
            }
            item {
                AboutInfoCard(
                    icon = Icons.Outlined.School,
                    title = "Final year project",
                    body = "Built as a final year undergraduate project. Model trained in PyTorch and deployed via ONNX Runtime for Android."
                )
            }
        }
    }
}

@Composable
fun AboutInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(22.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Text(
                    body, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutPreviewScreen(){
    TomatoGuardTheme {
        AboutScreen(onBack = {})
    }
}