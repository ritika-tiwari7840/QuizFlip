plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args) }

android {
    namespace = "com.ritika.quizflip"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ritika.quizflip"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    // Navigation components
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.4")

    // Existing dependencies...
    implementation (libs.androidx.core.ktx.v1120)
    implementation (libs.androidx.appcompat.v161)
    implementation (libs.material.v1100)
    implementation (libs.androidx.constraintlayout.v214)
    implementation (libs.androidx.lifecycle.livedata.ktx.v270)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v270)
    implementation (libs.navigation.fragment.ktx.v274)

    implementation (libs.navigation.ui.ktx.v274)

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation (libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
}