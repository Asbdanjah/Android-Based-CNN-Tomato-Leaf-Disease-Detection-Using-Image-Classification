
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asbdanja.tomatoguard"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.asbdanja.tomatoguard"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    androidResources {
        noCompress += listOf("onnx", "data")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
        }
        // ─── ADD THIS LINE TO FIX 16 KB ALIGNMENT ISSUE ───
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    // ── Jetpack Compose (Using the BOM from your TOML) ──────────────
    val bom = platform(libs.androidx.compose.bom)
    implementation(bom)
    androidTestImplementation(bom)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // This one is large, only keep if you need the extra icons
    implementation(libs.androidx.material.icons.extended)

    // ── Core & Lifecycle (Using TOML aliases) ───────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)  // Downgraded

    // These aren't in your TOML yet, so hardcoded is fine for now
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ── Navigation ──────────────────────────────────────────────────
    implementation(libs.androidx.navigation.compose)

    // ── ONNX Runtime (AI Model Execution) ───────────────────────────
    implementation(libs.onnxruntime.android)

    // ── Test & Debug ────────────────────────────────────────────────
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
}