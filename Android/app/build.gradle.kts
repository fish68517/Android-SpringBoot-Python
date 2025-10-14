plugins {
    id("com.android.application")
}

android {
    namespace = "com.archive.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.archive.app"
        minSdk = 25
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
}

dependencies {
    // implementation(files("libs/SparkChain.aar"))
    implementation("androidx.appcompat:appcompat:1.7.0")
    // implementation("com.hjq:xxpermissions:18.5")
    // implementation("com.hjq:xxpermissions:8.2")

    implementation("com.google.android.material:material:1.12.0")
    implementation("com.github.getActivity:XXPermissions:8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")




    // implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")




    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // Stomp Protocol for Android
    implementation ("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")

    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    // MPAndroidChart

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    /*   // 高德地图 SDK (基础地图)
   implementation ("com.amap.api:maps:9.9.0")// 替换为高德官网推荐的最新稳定版本

   // 高德地图定位 SDK (获取当前位置)
   implementation ("com.amap.api:location:6.3.0") // 替换为高德官网推荐的最新稳定版本*/

    implementation("com.amap.api:3dmap:latest.integration")

    // ViewModel and LiveData (for MVVM architecture)
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.2")

    // 高德地图搜索 SDK (已禁用)
    // implementation("com.amap.api:search:latest.integration")


    // 如果您需要其他高德服务，例如搜索、导航等，请根据需要添加：
    // implementation 'com.amap.api:search:9.9.0' // 搜索 SDK
    // implementation 'com.amap.api:navi-sdk:9.9.0' // 导航 SDK

}