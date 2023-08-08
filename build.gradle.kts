plugins {
    id("java")
    id("jacoco")
    id("net.ltgt.errorprone") version "3.1.0"
    id("checkstyle")
}

group = "io.deeplay.grandmastery"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "java")
    apply(plugin = "net.ltgt.errorprone")
    apply(plugin = "jacoco")
    apply(plugin = "checkstyle")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("org.slf4j:slf4j-simple:2.0.7")

        errorprone("com.google.errorprone:error_prone_core:2.20.0")

        testImplementation(platform("org.junit:junit-bom:5.9.3"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.mockito:mockito-core:5.4.0")
    }

    checkstyle {
        toolVersion = "10.3.3"
    }

    tasks.withType<Checkstyle>().configureEach {
        maxErrors = 0
        maxWarnings = 0
        reports {
            html.required.set(true)
            xml.required.set(false)
        }
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(false)
        }
    }
}
