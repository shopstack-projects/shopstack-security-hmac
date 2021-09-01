import java.util.*

plugins {
    java
    id("io.freefair.lombok") version Versions.lombokPlugin
    checkstyle
    jacoco
    `maven-publish`
    signing
}

version = "1.0.0.RELEASE"

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
    implementation(Dependencies.apacheCommonsCodec)

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

publishing {

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "dev.shopstack.security"
            artifactId = "shopstack-security-hmac"

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

            val baseUri = "https://s01.oss.sonatype.org/content/repositories"
            val releasesRepo = uri("$baseUri/releases")
            val snapshotsRepo = uri("$baseUri/snapshots")
            url = if (isReleaseBuild()) releasesRepo else snapshotsRepo

            credentials(PasswordCredentials::class)
        }
    }
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

    signing {
        isRequired = isReleaseBuild()

        val signingKey: String? by project
        val signingPassword: String? by project

        if (signingKey != null) {
            useInMemoryPgpKeys(base64Decode(signingKey), base64Decode(signingPassword))
            sign(publishing.publications["mavenJava"])
        }
    }

}

fun isReleaseBuild() =
    version.toString().contains("RELEASE")

fun base64Decode(s: String?) =
    s?.let { String(Base64.getDecoder().decode(it)).trim() }
