import org.gradle.kotlin.dsl.implementation

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.thoughts_cleaning"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.thoughts_cleaning"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            resValue("string", "kakao_login_native_key", "aa101db5c64ffa1322eb95b0904196a3")
            resValue("string", "kakao_login_native_key_MANIFEST", "kakaoaa101db5c64ffa1322eb95b0904196a3")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue("string", "kakao_login_native_key", "aa101db5c64ffa1322eb95b0904196a3")
            resValue("string", "kakao_login_native_key_MANIFEST", "kakaoaa101db5c64ffa1322eb95b0904196a3")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.window)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // 2. Remove the versions from individual Compose libraries
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.activity:activity-ktx:1.10.0")
    implementation("androidx.activity:activity-compose:1.10.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation(project(":joystick2"))

    // Navigation Fragment KTX (Kotlin extension)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    // Navigation UI KTX (BottomNavigationView 등 UI 컴포넌트 연동)
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    implementation("androidx.window:window:1.2.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.core:core-ktx:1.12.0")

    implementation("com.google.code.gson:gson:2.13.2")

    implementation("androidx.viewpager2:viewpager2:1.1.0")

    implementation("me.relex:circleindicator:2.1.6")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// 최신 버전에 맞게 조정
// Gson Converter (JSON 데이터 파싱)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
// OkHttp (선택 사항: 로깅 인터셉터 등 고급 설정 시 유용)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
// 최신 버전에 맞게 조정
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
// 디버깅용

    //네이버 소셜 로그인
    implementation("com.navercorp.nid:oauth-jdk8:5.9.1")
    // jdk 8
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-user:2.23.0")

// Glide Library
    val glideVersion = "4.9.0" // 버전을 변수로 관리하는 것을 권장합니다.

    implementation("com.github.bumptech.glide:glide:$glideVersion")

    // Annotation Processor (코틀린 프로젝트에서는 kapt를 사용해야 합니다)
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    implementation("com.android.support:support-compat:27.1.1")
}