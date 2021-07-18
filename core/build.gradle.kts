plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}


android {
    compileSdkVersion(Sdk.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Sdk.minSdkVersion)
        targetSdkVersion(Sdk.targetSdkVersion)
        versionCode = 3
        versionName = "1.2"
    }

    buildTypes {
        getByName("debug") {
            buildConfigField(
                "String",
                "MOVIES_DB_API",
                project.properties["movieDBApiToken"] as String
            )
        }
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField(
                "String",
                "MOVIES_DB_API",
                project.properties["movieDBApiToken"] as String
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // work-runtime-ktx 2.1.0 and above now requires Java 8
    kotlinOptions {
        jvmTarget = "1.8"

        // Enable Coroutines and Flow APIs
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.FlowPreview"
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$KOTLIN_VERSION")

    //Coroutines
    implementation(Coroutines.COROUTINES_ANDROID)
    implementation(Coroutines.COROUTINES_CORE)

    //ROOM
    implementation(ArchitectureComponent.Room.ROOM_RUNTIME)
    implementation(ArchitectureComponent.Room.ROOM_KTX)
    kapt(ArchitectureComponent.Room.ROOM_COMPILER)

    //Hilt
    implementation(ArchitectureComponent.Hilt.HILT_ANDROID)
    kapt(ArchitectureComponent.Hilt.HILT_COMPILER)

    //Paging library
    implementation(ArchitectureComponent.PagingLibrary.PAGING)

    //DataStore
    implementation(ArchitectureComponent.DataStore.DATASTORE)

    //Extra libraries
    //Glide
    implementation(Glide.GLIDE)
    kapt(Glide.COMPILER)

    //Retrofit
    implementation(Retrofit.RETROFIT)
    implementation(Retrofit.CONVERTER_GSON)
    implementation(Retrofit.LOGGING_INTERCEPTOR)


    //Testing dependencies
    androidTestImplementation(TestingDependencies.CORE_TESTING)
    androidTestImplementation(TestingDependencies.ESPRESSO_CORE)
    androidTestImplementation(TestingDependencies.JUNIT_EXT)
    androidTestImplementation(TestingDependencies.RULES)
    androidTestImplementation(TestingDependencies.RUNNER)
    androidTestImplementation(TestingDependencies.ROOM)
    androidTestImplementation(TestingDependencies.HILT_ANDROID)
    kaptAndroidTest(TestingDependencies.HILT_ANDROID_COMPILER)
    testImplementation(TestingDependencies.JUNIT)
}