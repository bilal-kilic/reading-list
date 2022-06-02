import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    java
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "bilalkilic.com"
version = "0.0.1"
application {
    mainClass.set("bilalkilic.com.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    maven { url = uri("https://jcenter.bintray.com") }
}

tasks {
    register("fatJar", Jar::class.java) {
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to "bilalkilic.com.ApplicationKt")
        }
        from(configurations.runtimeClasspath.get()
            .onEach { println("add from dependencies: ${it.name}") }
            .map { if (it.isDirectory) it else zipTree(it) })
        val sourcesMain = sourceSets.main.get()
        sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
        from(sourcesMain.output)
    }
}

dependencies {
    implementation(files("src/main/resources/lib/kompendium-core-1.7.0-SNAPSHOT.jar"))
    implementation(group = "org.webjars", name = "swagger-ui", version = "3.47.1")

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-compression:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-webjars:2.0.0-beta-1")
    implementation("org.webjars:jquery:3.2.1")
    implementation("io.ktor:ktor-server-call-logging:2.0.0-beta-1")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("io.insert-koin:koin-ktor:3.1.5")
    implementation("com.trendyol:kediatr-koin-starter:1.0.0")
    implementation("de.siegmar:logback-gelf:3.0.0")
    implementation("io.ktor:ktor-webjars:1.6.7")
    implementation("io.ktor:ktor-server-data-conversion:2.0.0-beta-1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.couchbase.lite:couchbase-lite-java:3.0.0-beta02")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.apptastic:rssreader:2.5.0")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.umehara:ogmapper:1.0.0")
    implementation("com.rometools:rome:1.18.0")
    implementation("io.github.config4k:config4k:0.4.2")
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}