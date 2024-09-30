plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "msi.crool.saptap"
    compileSdk = 34



    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(11) // or 17
        }
    }

    defaultConfig {
        applicationId = "msi.crool.saptap"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding=true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += listOf(
                "/META-INF/androidx.localbroadcastmanager_localbroadcastmanager.version",
                "/META-INF/*.version",
                "/META-INF/*.kotlin_module",
            )
        }
    }
}

dependencies {
    // Circular ImageView Rotation
    implementation("com.github.annhienktuit:CircularImageviewRotation:1.0.8") {
        exclude(group = "com.android.support", module = "support-compat")
    }


    // Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")

    // RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView dependency
    implementation("androidx.cardview:cardview:1.0.0")

    // Material Design dependency
    implementation("com.google.android.material:material:1.12.0")

    // ConstraintLayout dependency
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // AppCompat dependency
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Core KTX
    implementation("androidx.core:core-ktx:1.13.1")

    // Lifecycle runtime KTX
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")

    // Activity Compose (No need for `androidx.activity:activity` separately)
    implementation("androidx.activity:activity-compose:1.9.2")

    // Compose BOM (Bill of Materials)
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))

    // Jetpack Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material3
    implementation("androidx.compose.material3:material3")

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Compose UI test dependencies
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.0")

    // Debug dependencies
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

