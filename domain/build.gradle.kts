import Dependencies.addCoreModuleDependencies
import Dependencies.addDomainDependencies
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
        buildConfigField("String", "API_BASE_URL", "\"https://data.cityofnewyork.us/resource/\"")
        buildConfigField("String", "APP_TOKEN", project.property("APP_TOKEN") as String)
        buildConfigField("String", "API_KEY", project.property("API_KEY") as String)
        buildConfigField("String", "DATABASE_NAME", "\"app-db\"")
        buildConfigField("int", "DATABASE_VERSION", "1")
        buildConfigField("boolean", "DATABASE_EXPORT_SCHEMA", "false")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    addDomainDependencies()
    addTestDependencies()
}