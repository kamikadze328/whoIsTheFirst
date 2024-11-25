plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.android.ksp) apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:${libs.versions.agp}")
        classpath(libs.huawei.plugin)
    }
}
