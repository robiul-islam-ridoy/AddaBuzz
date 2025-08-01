import java.util.Properties
import java.io.FileInputStream
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(FileInputStream(file))
    }
}

val cloudName: String = localProperties.getProperty("CLOUDINARY_CLOUD_NAME") ?: ""
val apiKey: String = localProperties.getProperty("CLOUDINARY_API_KEY") ?: ""
val apiSecret: String = localProperties.getProperty("CLOUDINARY_API_SECRET") ?: ""


android {
    namespace = "com.example.addabuzz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.addabuzz"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"$cloudName\"")
        buildConfigField("String", "CLOUDINARY_API_KEY", "\"$apiKey\"")
        buildConfigField("String", "CLOUDINARY_API_SECRET", "\"$apiSecret\"")
    }
    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.cloudinary:cloudinary-android:2.3.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

}