import com.android.build.gradle.internal.ide.dependencies.getBuildPath

plugins {
    alias(libs.plugins.android.application)
    //    id("com.android.application")
    id("com.google.gms.google-services")
}

buildscript {
    repositories {
        /*
        maven {
            url = uri("http://dl.bintray.com/amulyakhare/maven")
            isAllowInsecureProtocol = true
        }

         */
        maven { url = uri("https://jitpack.io") }
    }
    /*
    dependencies {
        classpath("com.github.alvinhkh:TextDrawable:c1c2b5b")
    }
     */
}


android {
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
        )
        )
    }

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
    implementation(libs.androidx.preference)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.annotation:annotation:1.6.0")
    testImplementation(libs.espresso.intents)
    testImplementation(libs.ext.junit)
    testImplementation(libs.test.core)
    testImplementation(libs.ext.junit)
    testImplementation(libs.test.core)
    testImplementation(libs.monitor)
    testImplementation(libs.ext.junit)
    testImplementation(libs.espresso.core)
    testImplementation(libs.androidx.core)
    implementation(libs.ext.junit)
    // Espresso and Testing Dependencies
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.4.0")

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
    androidTestImplementation("com.google.firebase:firebase-firestore:24.4.0")
    implementation(libs.google.firebase.firestore)

    // Google Play Services dependencies
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
    implementation("com.google.android.gms:play-services-base:18.5.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.material:material:1.9.0")


    // ZXing dependencies for barcode scanning
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Unit testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.5.1")
    androidTestImplementation(libs.junit.jupiter)
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.5.1")

    // Intent testing dependencies
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation ("junit:junit:4.13.2")

    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test:core:1.4.0")

    // Mockito dependencies for unit testing
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-inline:5.5.0")
    testImplementation("org.mockito:mockito-android:5.5.0")

    // Google task dependencies
    testImplementation("com.google.android.gms:play-services-tasks:18.2.0")

    // Image profile defaults
    //implementation("com.amulyakhare:com.amulyakhare.textdrawable:1.0.1")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.github.alvinhkh:TextDrawable:c1c2b5b")

    // Uploading image
    implementation("com.google.firebase:firebase-storage:19.1.0")

}