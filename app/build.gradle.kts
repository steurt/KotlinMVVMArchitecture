import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {

    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "app.mvvm.architecture"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mutableMapOf(
                        Pair("room.incremental", "true"),
                        Pair("room.expandProjection", "true")
                    )
                )
            }
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }

        testOptions {
            unitTests.isReturnDefaultValues = true
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    val localProperties = Properties()
    localProperties.load(rootProject.file("local.properties").inputStream())

    val newsApiKey = "NEWS_API_KEY"

    flavorDimensions("default")
    productFlavors {
        create("dev") {
            applicationIdSuffix = ".dev"
            isDefault = true
            buildConfigString(newsApiKey, localProperties.getProperty("newsApiKeyDev"))
        }
        create("tst") {
            applicationIdSuffix = ".tst"
            buildConfigString(newsApiKey, localProperties.getProperty("newsApiKeyTst"))
        }
        create("acc") {
            applicationIdSuffix = ".acc"
            buildConfigString(newsApiKey, localProperties.getProperty("newsApiKeyAcc"))
        }
        create("prd") {
            buildConfigString(newsApiKey, localProperties.getProperty("newsApiKeyPrd"))
        }
    }
}

dependencies {

    // Kotlin
    val kotlinVersion: String by rootProject.extra
    implementation(kotlin("stdlib", kotlinVersion))

    // Coroutines
    val coroutinesVersion = "1.4.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    // AndroidX
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.fragment:fragment-ktx:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.browser:browser:1.3.0")

    // Navigation components
    val navigationVersion: String by rootProject.extra
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // Lifecycle components
    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-runtime:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // Android Material
    implementation("com.google.android.material:material:1.3.0")

    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Splitties
    val splittiesVersion = "3.0.0-beta01"
    implementation("com.louiscad.splitties:splitties-fun-pack-android-base:$splittiesVersion")
    implementation("com.louiscad.splitties:splitties-fun-pack-android-material-components:$splittiesVersion")
    implementation("com.louiscad.splitties:splitties-arch-lifecycle:$splittiesVersion")
    implementation("com.louiscad.splitties:splitties-typesaferecyclerview:$splittiesVersion")
    implementation("com.louiscad.splitties:splitties-exceptions:$splittiesVersion")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

    // OkHttp
    val okHttpVersion = "4.9.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // Conscrypt
    implementation("org.conscrypt:conscrypt-android:2.5.1")

    // Moshi
    val moshiVersion = "1.11.0"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    // Room
    val roomVersion = "2.2.5"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // KOIN
    val koinVersion = "2.2.2"
    implementation("org.koin:koin-android:$koinVersion")
    implementation("org.koin:koin-android-viewmodel:$koinVersion")

    // Unit testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // Mockk
    testImplementation("io.mockk:mockk:1.10.6")

    // Instrumentation testing
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.allWarningsAsErrors = true
}

// For flavors usage
fun com.android.build.gradle.internal.dsl.BaseFlavor.buildConfigBoolean(
    name: String,
    value: Boolean
) = buildConfigField("Boolean", name, value.toString())

fun com.android.build.gradle.internal.dsl.BaseFlavor.buildConfigString(
    name: String,
    value: String
) = buildConfigField("String", name, "\"$value\"")

fun com.android.build.gradle.internal.dsl.BaseFlavor.buildConfigInt(name: String, value: Int) =
    buildConfigField("int", name, "$value")