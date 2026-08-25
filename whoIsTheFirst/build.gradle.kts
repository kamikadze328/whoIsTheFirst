import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kamikadze328.whoisthefirst"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.kamikadze328.whoisthefirst"
        minSdk = 23
        targetSdk = 37
        versionCode = 28
        versionName = "1.28"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Google Service
    implementation(libs.app.update.ktx)

    // huawei service
    implementation(libs.appservice)

    // dagger
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}