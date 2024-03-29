plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.cs446group18.delaywise"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.cs446group18.delaywise"
        minSdk = 27
        targetSdk = 33
        versionCode = 5
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    flavorDimensions += "environment"
    productFlavors {
        create("local") {
            dimension = "environment"
            buildConfigField("String", "API_SERVER_URL", "\"http://10.0.2.2:8082\"")
        }
        create("production") {
            dimension = "environment"
            buildConfigField("String", "API_SERVER_URL", "\"https://delaywise.mcnamee.io\"")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.1")
    implementation("com.android.volley:volley:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.navigation:navigation-compose:2.4.0")
    implementation("androidx.paging:paging-compose:1.0.0-alpha02")
    implementation("androidx.room:room-ktx:2.4.0")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.material:material-icons-core") // Optional
    implementation("androidx.compose.material:material-icons-extended") // Optional
    implementation("androidx.compose.material3:material3-window-size-class") // Optional

    implementation("androidx.activity:activity-compose:1.6.1") // Optional
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1") // Optional
    implementation("androidx.compose.runtime:runtime-livedata") // Optional
    implementation("androidx.compose.runtime:runtime-rxjava2") // Optional

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("androidx.compose.ui:ui-text-google-fonts:1.3.2")

    implementation("io.ktor:ktor-client-core:2.2.3")
    implementation("io.ktor:ktor-client-cio:2.2.3")
    implementation("io.ktor:ktor-client-okhttp:2.2.3")
    implementation("io.ktor:ktor-client-logging:2.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("io.github.raamcosta.compose-destinations:core:1.8.35-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.8.35-beta")

    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("com.opencsv:opencsv:4.6")
    implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:2.0.0")

    // Room
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    implementation(project(":lib"))

    // Includes the core logic for charts and other elements.
    implementation("com.patrykandpatrick.vico:core:1.6.4")

    // For Jetpack Compose.
    implementation("com.patrykandpatrick.vico:compose:1.6.4")

    // For the view system.
    implementation("com.patrykandpatrick.vico:views:1.6.4")

    // For `compose`. Creates a `ChartStyle` based on an M2 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m2:1.6.4")

    // For `compose`. Creates a `ChartStyle` based on an M3 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m3:1.6.4")
}

kapt {
    correctErrorTypes = true
}
