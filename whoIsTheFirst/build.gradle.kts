plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.android.ksp)
}

android {
    namespace = "com.kamikadze328.whoisthefirst"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kamikadze328.whoisthefirst"
        minSdk = 22
        targetSdk = 35
        versionCode = 25
        versionName = "1.26"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Google Service
    implementation(libs.app.update.ktx)

    // huawei service
    implementation(libs.agconnect.core)
    implementation(libs.appservice)
    implementation(libs.hianalytics)

    // dagger
    implementation(libs.dagger)
    implementation(libs.dagger.android)
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)
}