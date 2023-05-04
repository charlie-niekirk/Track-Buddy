// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
        kotlinOptions {
            if (project.findProperty("trackbuddy:enableComposeMetrics") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination="
                            + project.buildDir.absolutePath + "/compose_metrics"
                )
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination="
                            + project.buildDir.absolutePath + "/compose_metrics"
                )
            }
        }
    }
}