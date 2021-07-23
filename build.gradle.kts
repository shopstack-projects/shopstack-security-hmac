plugins {
    java
    id("io.freefair.lombok") version Versions.lombokPlugin
    checkstyle
    jacoco
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.slf4jApi)
    testImplementation(Dependencies.slf4jSimpleLogger)

    testImplementation(Dependencies.junitJupiterApi)
    testRuntimeOnly(Dependencies.junitJupiterEngine)
    testImplementation(Dependencies.junitJupiterParams)
    testImplementation(Dependencies.assertjCore)
    testImplementation(Dependencies.mockitoInline)
    testImplementation(Dependencies.mockitoJunitJupiter)
}

checkstyle {
    toolVersion = Versions.checkstyle
}

jacoco {
    toolVersion = Versions.jacoco
}



tasks {
    test {
        useJUnitPlatform {
            maxParallelForks = Runtime.getRuntime().availableProcessors()
        }
        failFast = true
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
        }
    }
}

publishing {
    val artifactVersion = "1.0.0"

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "dev.shopstack.security"
            artifactId = "shopstack-security-hmac"
            version = artifactVersion

            from(components["java"])

            pom {
                name.set("Shopstack Security HMAC")
                description.set("Authenticate Shopify requests using the provided HMAC.")
                url.set("https://shopstack.dev")
                scm {
                    connection.set("scm:git:git://github.com/shopstack-projects/shopstack-security-hmac.git")
                    developerConnection.set("scm:git:ssh://github.com/shopstack-projects/shopstack-security-hmac.git")
                    url.set("https://github.com/shopstack-projects/shopstack-security-hmac/tree/main")
                }
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("alanguerin")
                        name.set("Alan Guerin")
                        email.set("hello@alanguerin.com")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "SonatypeOSS"
            val sonatypeBaseUri = "https://s01.oss.sonatype.org/content/repositories"
            val releasesRepo = uri("$sonatypeBaseUri/releases")
            val snapshotsRepo = uri("$sonatypeBaseUri/snapshots")
            url = snapshotsRepo // FIXME if (artifactVersion.endsWith("RELEASE")) releasesRepo else snapshotsRepo
        }
    }
}