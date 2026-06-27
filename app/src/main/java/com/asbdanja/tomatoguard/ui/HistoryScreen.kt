package com.asbdanja.tomatoguard.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Added import
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asbdanja.tomatoguard.ml.PredictionResult
import com.asbdanja.tomatoguard.theme.ForestGreen
import com.asbdanja.tomatoguard.theme.TomatoRed
import com.asbdanja.tomatoguard.viewmodel.ScanViewModel

// ── History Screen ────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: ScanViewModel,
    onBack: () -> Unit
) {
    val history by viewModel.history.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title  = { Text("Scan History") },
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
                containerColor    = ForestGreen,
                titleContentColor = Color.White
            )
        )
        if (history.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.History, contentDescription = null,
                        tint     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp))
                    Text("No scans yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(history) { result ->
                    HistoryItem(
                        result = result,
                        onShare = {
                            // ─── SHARING LOGIC ───
                            val shareBody = """
                                TomatoGuard Health Report
                                -------------------------
                                Diagnosis: ${result.label}
                                Confidence: ${(result.confidence * 100).toInt()}%
                                Status: ${if (result.isHealthy) "Healthy" else "Diseased"}
                                
                                Sent via TomatoGuard AI App.
                            """.trimIndent()

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Tomato Leaf Diagnosis")
                                putExtra(Intent.EXTRA_TEXT, shareBody)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share with Consultant"))
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun HistoryItem(result: PredictionResult, onShare: () -> Unit) {
    val dotColor = if (result.isHealthy) Color(0xFF43A047) else TomatoRed
    val badgeBg  = if (result.isHealthy) Color(0xFFE8F5E9) else Color(0xFFFDECEA)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier              = Modifier.padding(14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(dotColor))
            Column(modifier = Modifier.weight(1f)) {
                Text(result.label, style = MaterialTheme.typography.titleMedium)
                Text("Confidence: ${(result.confidence * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
            }

            // ─── SHARE BUTTON ───
            IconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = ForestGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(if (result.isHealthy) "Healthy" else "Diseased",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = dotColor)
            }
        }
    }
}