plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.popcorn"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.popcorn"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Deps.core_ktx)
    implementation(Deps.app_compat)
    implementation(Deps.android_material)
    implementation(Deps.constraint_layout)
    implementation(Deps.livedata)
    implementation(Deps.viewmodel)
    implementation(Deps.nav_fragment)
    implementation(Deps.nav_ui)
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.test_ext)
    androidTestImplementation(Deps.test_espresso_core)

    // retrofit2
    implementation(Deps.retrofit)
    implementation(Deps.retrofit_moshi)
    implementation(Deps.retrofit_logging)


    // coroutines
    implementation(Deps.coroutines_android)

    // dagger hilt
    implementation(Deps.dagger_hilt)
    kapt(Deps.dagger_hilt_compiler)

    //moshi
    ksp(Deps.moshi_codegen)
    implementation(Deps.moshi_converter)
    implementation(Deps.moshi)

    // recyclerview
    implementation(Deps.recyclerview)

    // card view
    implementation(Deps.card_view)

    //glide
    implementation(Deps.glide)
    annotationProcessor(Deps.glide_compiler)

    implementation(Deps.view_pager2)

}


kapt {
    correctErrorTypes = true
}