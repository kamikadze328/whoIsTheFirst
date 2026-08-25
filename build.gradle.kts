plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.huawei.plugin)
    }
}
