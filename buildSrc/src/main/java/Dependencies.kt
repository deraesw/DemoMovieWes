const val KOTLIN_VERSION = "1.4.32"

object Sdk {
    val compileSdkVersion = 30
    val minSdkVersion = 21
    val targetSdkVersion = 30
}

object Support {
    const val MATERIAL = "com.google.android.material:material:1.3.0"
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.3.0"
    const val GRIDLAYOUT = "androidx.gridlayout:gridlayout:1.0.0"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:1.4.3"
}

object ArchitectureComponent {

    object DataStore {
        private const val VERSION = "1.0.0-beta01"

        const val DATASTORE = "androidx.datastore:datastore-preferences:$VERSION"
    }

    object Hilt {
        const val VERSION = "2.35"

        const val HILT_ANDROID = "com.google.dagger:hilt-android:$VERSION"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:$VERSION"
    }

    object Lifecycle {
        const val RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01"
        const val EXTENSIONS = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    }

    object Navigation {
        private const val VERSION = "2.3.5"

        const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:$VERSION"
        const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:$VERSION"
    }

    object PagingLibrary {
        private const val VERSION = "3.0.0"

        const val PAGING = "androidx.paging:paging-runtime-ktx:$VERSION"
    }

    object Room {
        const val VERSION = "2.5.0"

        const val ROOM_RUNTIME = "androidx.room:room-runtime:$VERSION"
        const val ROOM_KTX = "androidx.room:room-ktx:$VERSION"
        const val ROOM_COMPILER = "androidx.room:room-compiler:$VERSION"
    }

    object WorkManager {
        const val VERSION = "2.5.0"

        const val WORK = "androidx.work:work-runtime-ktx:$VERSION"
    }
}


object Coroutines {
    private const val VERSION = "1.4.3"

    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
}

object Glide {
    private const val VERSION = "4.10.0"

    const val GLIDE = "com.github.bumptech.glide:glide:$VERSION"
    const val COMPILER = "com.github.bumptech.glide:compiler:$VERSION"
}

object Retrofit {
    private const val VERSION = "2.9.0"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:$VERSION"
    const val CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:$VERSION"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:$VERSION"
}

object TestingDependencies {
    const val CORE_TESTING = "androidx.arch.core:core-testing:2.1.0"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.3.0"
    const val JUNIT_EXT = "androidx.test.ext:junit:1.1.2"
    const val RULES = "androidx.test:rules:1.3.0"
    const val RUNNER = "androidx.test:runner:1.3.0"
    const val ROOM = "androidx.room:room-testing:${ArchitectureComponent.Room.VERSION}"
    const val WORK = "androidx.work:work-testing:${ArchitectureComponent.WorkManager.VERSION}"
    const val HILT_ANDROID = "com.google.dagger:hilt-android-testing:2.35"
    const val HILT_ANDROID_COMPILER =
        "com.google.dagger:hilt-android-compiler:${ArchitectureComponent.Hilt.VERSION}"
    const val JUNIT = "junit:junit:4.13.2"
}