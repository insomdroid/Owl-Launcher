plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.protobuf")
}

val sysuiLibs = listOf("animationlib", "contextualeducationlib", "iconloaderlib", "msdllib")

android {
    namespace = "com.android.launcher3"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.insomdroid.owl"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            // Size-only candidate. Keep debug as the install/test baseline until the
            // minified artifact has been verified on-device.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xjvm-default=all"
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("../AndroidManifest.xml")
            java.setSrcDirs(
                listOf("../src", "../src_plugins", "../src_no_quickstep", "../src_build_config", "../dagger_src", "../shims", "../concurrent_src", "../compose_disabled", "../shared_src") +
                    sysuiLibs.map { "../libs_systemui/$it/src" }
            )
            res.setSrcDirs(listOf("../res") + sysuiLibs.map { "../libs_systemui/$it/res" })
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.core:core-animation:1.0.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")
    implementation("androidx.window:window:1.3.0")
    implementation("com.google.guava:guava:33.3.1-android")
    implementation("androidx.graphics:graphics-shapes:1.0.1")
    implementation("androidx.slice:slice-core:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.25.5")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.dagger:dagger:2.52")
    kapt("com.google.dagger:dagger-compiler:2.52")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
