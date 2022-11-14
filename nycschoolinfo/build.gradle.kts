import Dependencies.addCoreModuleDependencies
import Dependencies.addCoreUiDependencies
import Dependencies.addTestDependencies

plugins {
    id("com.android.library")
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {

    compileSdk = AppConfig.compileSdk
    buildToolsVersion = AppConfig.buildTools

    defaultConfig {
        minSdk = AppConfig.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        getByName("test").java.srcDirs("src/test/java/com/education/nycschools/common")
    }

    testOptions { unitTests { all { it.maxHeapSize = "1024m" } } }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    packagingOptions {
        exclude("META-INF/notice.txt")
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    addCoreModuleDependencies()
    addCoreUiDependencies()
    addTestDependencies()
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":uicomponents"))
    testImplementation(project(":common"))
}