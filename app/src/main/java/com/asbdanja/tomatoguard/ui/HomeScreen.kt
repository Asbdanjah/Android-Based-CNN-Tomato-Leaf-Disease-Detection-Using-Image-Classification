package com.asbdanja.tomatoguard.ui

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asbdanja.tomatoguard.R
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asbdanja.tomatoguard.components.ErrorCard
import com.asbdanja.tomatoguard.components.InfoBannerCard
import com.asbdanja.tomatoguard.components.LoadingCard
import com.asbdanja.tomatoguard.components.ResultCard
import com.asbdanja.tomatoguard.components.ScanCard
import com.asbdanja.tomatoguard.theme.ForestGreen
import com.asbdanja.tomatoguard.theme.LeafGreen
import com.asbdanja.tomatoguard.viewmodel.ScanState
import com.asbdanja.tomatoguard.viewmodel.ScanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ScanViewModel) {

    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.finish()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    // ─── ADD THIS BLOCK TO CHANGE STATUS BAR COLOR ───
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set the status bar color to your ForestGreen
            window.statusBarColor = ForestGreen.toArgb()

            // This ensures the icons (clock, battery, wifi) are white
            // Set to 'true' if you want dark icons (for light backgrounds)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.analyzeImage(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        modifier = Modifier.size(34.dp), 
                        shape = RoundedCornerShape(8.dp),
                        color = LeafGreen
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tomatoguard_app_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Column {
                        Text("TomatoGuard", style = MaterialTheme.typography.titleMedium)
                        Text("Disease Detection", style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f))
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ForestGreen,
                titleContentColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (val s = state) {
                    is ScanState.Idle    -> ScanCard { imagePickerLauncher.launch("image/*") }
                    is ScanState.Loading -> LoadingCard()
                    is ScanState.Success -> ResultCard(
                        result = s.result,
                        bitmap = s.bitmap,
                        onRescan = { viewModel.reset() }
                    )
                    is ScanState.Error   -> ErrorCard(s.message) { viewModel.reset() }
                }
            }
            item { InfoBannerCard() }
        }
    }
}


@Preview(showBackground = true, name = "Home - Idle State")
@Composable
fun HomeScreenIdlePreview() {
    // We wrap this in a Column to mimic the HomeScreen layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background
    ) {
        // Mock Top Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            color = ForestGreen
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(start = 16.dp)) {
                Text("TomatoGuard", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Content
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Preview the ScanCard (Idle state)
            ScanCard(onScan = {})

            // Preview the InfoBanner
            InfoBannerCard()

            
            
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun LoadingStatePreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        LoadingCard()
    }
}