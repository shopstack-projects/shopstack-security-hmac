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

    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "dev.shopstack.security"
            artifactId = "shopstack-security-hmac"
            version = "1.0.0"

            from(components["java"])

            pom {
                name.set("Shopstack Security HMAC")
                description.set("A library to authenticate Shopify requests using the provided HMAC.")
                url.set("https://shopstack.dev")
                scm {
                    connection.set("scm:git:git://github.com/shopstack-projects/shopstack-security-hmac.git")
                    developerConnection.set("scm:git:ssh://github.com/shopstack-projects/shopstack-security-hmac.git")
                    url.set("https://github.com/shopstack-projects/shopstack-security-hmac/")
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
                        email.set("alan@alanguerin.com")
                    }
                }
            }
        }
    }
}