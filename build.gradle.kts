import kotlin.collections.*

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"

}

group "cn.org.wangchangjiu"
version "1.0"

repositories {
    mavenLocal()
    maven { url = uri("https://repo1.maven.org/maven2/") }
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("cn.org.wangchangjiu:sqltomongo-converter:1.0.2.1-RELEASE")
    implementation(kotlin("stdlib"))
}

// Configure Gradle IntelliJ Pluginunresolved
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.4")
    type.set("IC") // Target IDE Platform

    //plugins.set(listOf(/* Plugin Dependencies */))
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("221")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    jar {
        from(configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { file(it) })
    }

}


