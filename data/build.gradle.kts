plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.popcorn.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val baseUrl: String by project
        val apiKey: String by project
        val imageUrl: String by project
        val backdropUrl: String by project
        buildConfigField("String", "BASE_URL", baseUrl)
        buildConfigField("String", "API_KEY", apiKey)
        buildConfigField("String", "IMAGE_URL", imageUrl)
        buildConfigField("String", "BACKDROP_URL", backdropUrl)

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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(Deps.kotlin)
    implementation(Deps.core_ktx)
    implementation(Deps.retrofit)
    implementation(Deps.retrofit_logging)
    implementation(Deps.retrofit_moshi)

    implementation(Deps.coroutines_android)

    // dagger hilt
    implementation(Deps.dagger_hilt)
    kapt(Deps.dagger_hilt_compiler)

    ksp(Deps.moshi_codegen)
    implementation(Deps.moshi_converter)
    implementation(Deps.moshi)

    implementation(Deps.room)
    kapt(Deps.room_compiler)
    implementation(Deps.room_ktx)
}

kapt {
    correctErrorTypes = true
}