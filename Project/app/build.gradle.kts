plugins {
    alias(libs.plugins.android.application)
    //    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.king_of_the_castle_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.king_of_the_castle_project"
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

    tasks.withType<Test> {
        useJUnitPlatform() // Make all tests use JUnit 5
    }
}

dependencies {
    testImplementation(libs.test.core)
    testImplementation(libs.ext.junit)
    testImplementation(libs.test.core)
    testImplementation(libs.monitor)
    testImplementation(libs.ext.junit)
    testImplementation(libs.espresso.core)
    testImplementation(libs.androidx.core)
    // Espresso and Testing Dependencies
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    //FOR NOTIF TESTING PURPOSES
    testImplementation( "org.mockito:mockito-core:5.+")
    androidTestImplementation ("org.mockito:mockito-android:5.+")
    testImplementation("org.assertj:assertj-core:3.22.0")

    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test:core:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")


    // Regular implementation dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation(libs.google.firebase.firestore)

    // Google Play Services dependencies
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
    implementation("com.google.android.gms:play-services-base:18.5.0")

    // ZXing dependencies for barcode scanning
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Unit testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.0.1")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.0.1")

    // Intent testing dependencies
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation ("junit:junit:4.13.2")
    testImplementation (libs.mockito.core)
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test:core:1.4.0")
}

