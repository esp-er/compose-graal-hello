import org.gradle.kotlin.dsl.runtimeClasspath
import org.gradle.kotlin.dsl.singleFile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("org.jetbrains.compose") version "1.3.1"
    id("org.graalvm.buildtools.native") version "0.9.20"

    id("com.google.osdetector") version "1.7.3"
}

group = "com.example"
version = "1.0-SNAPSHOT"


//By default on Windows, disable the terminal pop-up window when running exe
val disableWindowsTerminal by extra(true)

//Name of ouptut image file
val executableName by extra("composegraal")


val isWindows: Boolean by extra { osdetector.os == "windows" }

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    gradlePluginPortal()
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("io.github.humbleui:jwm:0.4.13")
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "JWMAppKt"
    }
}

graalvmNative {
    toolchainDetection.set(false)
    binaries{
        named("main"){
            mainClass.set("JWMAppKt")
            imageName.set(executableName)
            buildArgs("-Djava.awt.headless=false")
        }
    }

    agent{
        defaultMode.set("standard")

        metadataCopy {
            inputTaskNames.add("run") // Tasks previously executed with the agent attached.
            outputDirectories.add("src/main/resources/META-INF/native-image")
            mergeWithExisting.set(true)
        }
    }
}

if(isWindows && disableWindowsTerminal) {
    tasks.getByName("nativeCompile").doLast {
        if (this.state.failure == null) {
            project.exec {
                commandLine("cmd", "/c", "script\\disableTerminal.bat", "$executableName.exe")
            }
            println("\n[disableTerminal.bat] Disabled Windows terminal window on generated executable $executableName.exe")
        }
    }
}




