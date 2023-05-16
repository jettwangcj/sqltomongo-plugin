import kotlin.collections.*

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.12.0"

}

group "cn.org.wangchangjiu"
version "1.0.1"

repositories {
    mavenLocal()
    maven { url = uri("https://repo1.maven.org/maven2/") }
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("cn.org.wangchangjiu:sqltomongo-converter:1.0.2.1-RELEASE")
}

// Configure Gradle IntelliJ Pluginunresolved
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.4")
    type.set("IU") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
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
        token.set(System.getenv("perm:amV0dHdhbmdjag==.OTItODA0Mg==.Kmujv4p1NQ7AgAvBShvuVpZStbYEP5"))
    }

    runPluginVerifier {
        ideVersions.set(listOf("2021.3.3"))
      //  localPaths.set(listOf(file("build/distributions/sqltomongo-plugin.zip")))
    }

    jar {
        from(configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { file(it) })
    }

}


