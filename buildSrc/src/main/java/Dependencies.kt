/**
 * To define version
 */
object Versions {

    const val gradlePlugin = "7.0.0"
    const val appCompat = "1.6.1"
    const val material = "1.8.0"
    const val constraintLayout = "2.1.4"
    const val jUnit = "4.12"
    const val kotlin = "1.5.21"
    const val hilt = "2.48"
    const val lifecycle = "2.6.1"
    const val arch = "2.1.0"
    const val core_ktx = "1.9.0"
    const val nav_version = "2.5.3"
    const val room_version = "2.4.0"
    const val moshi = "1.14.0"
    const val moshi_converter = "2.9.0"
    const val recycler_view = "1.3.2"
    const val cardview = "1.0.0"
    const val glide = "4.12.0"
    const val view_pager2 = "1.1.0-beta02"
    const val retrofit = "2.9.0"
    const val retrofit_logging = "4.9.1"
    const val junit = "4.13.2"
    const val espresso_core = "3.5.1"
    const val test_ext = "1.1.5"
    const val coroutine = "1.6.4"
}

/**
 * To define plugins
 */
object BuildPlugins {
    const val android_gradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hilt_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}

/**
 * To define dependencies
 */
object Deps {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val app_compat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val junit = "junit:junit:${Versions.junit}"

    const val test_espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    const val test_ext = "androidx.test.ext:junit:${Versions.test_ext}"
    const val android_material = "com.google.android.material:material:${Versions.material}"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val legacy_support = "androidx.legacy:legacy-support-v4:1.0.0"
    // ViewModel
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    // LiveData
    const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    // livedata_core
    const val livedata_core = "androidx.lifecycle:lifecycle-livedata-core-ktx:2.3.1"

    // retrofit2
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val retrofit_logging = "com.squareup.okhttp3:logging-interceptor:${Versions.retrofit_logging}"

    // moshi
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshi_converter = "com.squareup.retrofit2:converter-moshi:${Versions.moshi_converter}"
    const val moshi_codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"


    // dagger hilt
    const val dagger_hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val dagger_hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

    // recyclerview
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recycler_view}"

    // card view
    const val card_view = "androidx.cardview:cardview:${Versions.cardview}"

    const val room = "androidx.room:room-runtime:${Versions.room_version}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room_version}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room_version}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val nav_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_version}"
    const val nav_ui = "androidx.navigation:navigation-ui-ktx:${Versions.nav_version}"

    const val view_pager2 = "androidx.viewpager2:viewpager2:${Versions.view_pager2}"

    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
}
