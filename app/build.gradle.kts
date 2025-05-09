plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.dz_1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dz_1"
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

    configurations.all{
        resolutionStrategy{
            force("org.jetbrains:annotations:23.0.0")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.shimmer)
    implementation (libs.jetbrains.kotlinx.coroutines.android)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    //implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation(libs.retrofit)
    //implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.converter.gson)
    //implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation(libs.okhttp)
    //implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.gson)
    //implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation(libs.logging.interceptor)

}