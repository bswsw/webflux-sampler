plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("kapt") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"

    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "bsw"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val kotlinLoggingVersion = "3.0.5"
val mapstructVersion = "1.5.5.Final"

val asciidoctorExtensions: Configuration by configurations.creating

dependencies {
    implementation("io.projectreactor.kafka:reactor-kafka")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")

    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    kaptTest("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    implementation("io.github.microutils:kotlin-logging:${kotlinLoggingVersion}")
    runtimeOnly("io.github.microutils:kotlin-logging-jvm:${kotlinLoggingVersion}")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
}

tasks {
    val snippetsDir by extra {
        file("build/generated-snippets")
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    asciidoctor {
        dependsOn(test)
        configurations(asciidoctorExtensions.name)
        inputs.dir(snippetsDir)
    }

    val asciidoctorSourcePath = "${asciidoctor.get().outputDir}"
    val asciidoctorCopy by creating(Copy::class) {
        dependsOn(asciidoctor)
        from(asciidoctorSourcePath)
        into("src/main/resources/static/docs")
    }

    bootJar {
        dependsOn(asciidoctorCopy)
    }
}
