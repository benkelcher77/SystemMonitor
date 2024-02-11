import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "ca.kelcher"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
}

tasks.register(name = "compileNativeLibs", type = Exec::class) {
    description = "Generate libnvml-bindings.so and copy to the libs/ directory."

    val dir = "${project.projectDir.absolutePath}/src/native"

    // TODO Figure out how to load environment variables in Gradle.
    environment("JAVA_HOME", "/usr/lib/jvm/java-21-openjdk")
    commandLine("/bin/bash", "${dir}/build.sh")


}

tasks["jar"].dependsOn("compileNativeLibs")

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs += "-Djava.library.path=/home/ben/Dev/SystemMonitor/libs"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SystemMonitor"
            packageVersion = "1.0.0"
        }
    }
}

