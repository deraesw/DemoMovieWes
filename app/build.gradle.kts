plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Sdk.compileSdkVersion)

    defaultConfig {
        applicationId = "com.demo.developer.deraesw.demomoviewes"
        minSdkVersion(Sdk.minSdkVersion)
        targetSdkVersion(Sdk.targetSdkVersion)
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "com.demo.developer.deraesw.demomoviewes.CustomTestRunner"
        vectorDrawables.useSupportLibrary = true
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

    buildFeatures {
        dataBinding = true
    }


    flavorDimensions("default")

    productFlavors {
        create("prod") {
            dimension = "default"
            resValue("string", "app_name", "Movies App")
        }

        create("mock") {
            dimension = "default"
            applicationIdSuffix = ".mock"
            resValue("string", "app_name", "Movies App - Mock")
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$KOTLIN_VERSION")

    //Coroutines
    implementation(Coroutines.COROUTINES_ANDROID)
    implementation(Coroutines.COROUTINES_CORE)

    //Support dependencies
    implementation(Support.MATERIAL)
    implementation(Support.APPCOMPAT)
    implementation(Support.GRIDLAYOUT)
    implementation(Support.CONSTRAINT_LAYOUT)

    //Architecture dependency

    // ViewModel and LiveData
    implementation(ArchitectureComponent.Lifecycle.RUNTIME)
    implementation(ArchitectureComponent.Lifecycle.EXTENSIONS)

    //ROOM
    implementation(ArchitectureComponent.Room.ROOM_RUNTIME)
    implementation(ArchitectureComponent.Room.ROOM_KTX)
    kapt(ArchitectureComponent.Room.ROOM_COMPILER)

    //Work manager
    implementation(ArchitectureComponent.WorkManager.WORK)

    //Paging library
    implementation(ArchitectureComponent.PagingLibrary.PAGING)

    //Navigation
    implementation(ArchitectureComponent.Navigation.NAVIGATION_FRAGMENT)
    implementation(ArchitectureComponent.Navigation.NAVIGATION_UI)

    //Hilt
    implementation(ArchitectureComponent.Hilt.HILT_ANDROID)
    kapt(ArchitectureComponent.Hilt.HILT_COMPILER)

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
    androidTestImplementation(TestingDependencies.WORK)
    androidTestImplementation(TestingDependencies.HILT_ANDROID)
    kaptAndroidTest(TestingDependencies.HILT_ANDROID_COMPILER)
    testImplementation(TestingDependencies.JUNIT)

}
