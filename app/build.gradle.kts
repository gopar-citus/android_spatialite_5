import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "org.spatialite"
    compileSdk = 33
    group="org.spatialite"

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    ndkVersion = "23.1.7779620"
    sourceSets {
        getByName("main") {
            jniLibs.srcDir("src/main/jniLibs")
            jni.srcDirs("src/main/jni")
        }
    }
    externalNativeBuild {

        cmake {
            version = "3.22.1"
            path = file("src/main/jni/CMakeLists.txt")
        }
    }

    publishing {
        singleVariant("release") {
            version = "5.1.0"
        }
    }

}

tasks.register<Exec>("buildStaticLibs") {
    workingDir("${project.rootDir}/static_libs")
    commandLine("./masterbuild.sh")
    environment("NDK_HOME", project.extensions.getByType<com.android.build.gradle.BaseExtension>().ndkDirectory)
    environment("PROJECT_ROOT", project.rootDir)
    enabled=false
}


tasks.named("preBuild") {
    dependsOn("buildStaticLibs")
}




dependencies {

testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "org.spatialite"
            artifactId = "spatialite-android"
            version = "5.1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            val propsFile = rootProject.file("github.properties")
            val props = Properties()
            props.load(FileInputStream(propsFile))

            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/gopar-citus/spatialite-android")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: props.getProperty("gpr.user")
                password = project.findProperty("gpr.key") as String? ?: props.getProperty("gpr.key")
            }
        }
    }
}

