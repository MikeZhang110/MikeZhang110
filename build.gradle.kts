plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.translationpro"
version = "1.0.0-SNAPSHOT"

java {
    // Support Java 17-25
    val javaVersion = JavaVersion.current()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    // Only use toolchain if Java version is exactly 17
    // This allows building with newer Java versions
    if (javaVersion.majorVersion.toInt() == 17) {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX
    implementation("org.openjfx:javafx-controls:21.0.1")
    implementation("org.openjfx:javafx-fxml:21.0.1")
    implementation("org.openjfx:javafx-web:21.0.1")
    implementation("org.openjfx:javafx-swing:21.0.1")

    // Database
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Dependency Injection
    implementation("com.google.inject:guice:7.0.0")

    // Event Bus
    implementation("com.google.guava:guava:32.1.3-jre")

    // ICU for text segmentation
    implementation("com.ibm.icu:icu4j:74.1")

    // File format support
    implementation("org.apache.tika:tika-core:2.9.1")
    implementation("org.apache.tika:tika-parsers-standard-package:2.9.1")
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // XML processing
    implementation("org.jdom:jdom2:2.0.6.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.0")

    // HTTP client for MT services
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Utilities
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("commons-io:commons-io:2.15.1")
    implementation("commons-codec:commons-codec:1.16.0")

    // Spell checking
    implementation("com.gitlab.dumonts:hunspell-java:1.3.0")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing")
}

application {
    mainClass.set("com.translationpro.TranslationProApp")
    applicationDefaultJvmArgs = listOf(
        "-Xmx2048m",
        "-XX:+UseG1GC",
        "--add-exports", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf(
        "-Xlint:unchecked",
        "-Xlint:deprecation"
    ))
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = false
    }
}

tasks.shadowJar {
    archiveBaseName.set("translationpro")
    archiveClassifier.set("standalone")
    manifest {
        attributes(
            "Main-Class" to "com.translationpro.TranslationProApp",
            "Implementation-Title" to "TranslationPro",
            "Implementation-Version" to project.version
        )
    }
    // Merge service files for dependency injection
    mergeServiceFiles()
}

// Create Windows installer using jpackage
tasks.register("createInstaller") {
    dependsOn(tasks.shadowJar)
    doLast {
        val jarFile = tasks.shadowJar.get().archiveFile.get().asFile
        exec {
            commandLine(
                "jpackage",
                "--input", "build/libs",
                "--name", "TranslationPro",
                "--main-jar", jarFile.name,
                "--type", "msi",
                "--app-version", version.toString().replace("-SNAPSHOT", ""),
                "--vendor", "TranslationPro",
                "--description", "Advanced Computer-Assisted Translation Tool",
                "--win-dir-chooser",
                "--win-menu",
                "--win-shortcut"
            )
        }
    }
}

// Create portable ZIP distribution
tasks.register<Zip>("createPortable") {
    dependsOn(tasks.shadowJar)
    archiveBaseName.set("translationpro-portable")
    archiveVersion.set(project.version.toString())

    from(tasks.shadowJar.get().archiveFile) {
        rename { "TranslationPro.jar" }
    }
    from("README.md")
    from("LICENSE")

    into("translationpro-${project.version}")
}
