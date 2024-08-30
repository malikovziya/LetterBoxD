plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    // safe args
    id("androidx.navigation.safeargs.kotlin")

    // firebase
    id("com.google.gms.google-services")

    // parcelize
    id("kotlin-parcelize")
}



android {
    namespace = "com.example.letterboxd"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.letterboxd"
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
        buildConfig = true
    }
}

dependencies {
    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    // hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    implementation("androidx.activity:activity:1.9.1")
    implementation("androidx.compose.animation:animation-graphics-android:1.6.8")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")

    // glide?
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // firebase auth
    implementation("com.google.firebase:firebase-auth:23.0.0")

    // firebase firestore
    implementation("com.google.firebase:firebase-firestore:25.0.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // simple rating bar
    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")

    // room database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")


    // paging 3
    implementation("androidx.paging:paging-runtime:3.3.2")

    // photo picker
    implementation("androidx.activity:activity:1.9.1")

    // fancy toast
    implementation("io.github.shashank02051997:FancyToast:2.0.2")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.2.0-alpha01")

    // shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // recycler view swipe decorator
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")
}

// hilt
kapt {
    correctErrorTypes = true
}
