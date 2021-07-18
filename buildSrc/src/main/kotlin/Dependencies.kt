/**
 * Version reference for project dependencies.
 */
object Versions {
    const val lombokPlugin = "6.0.0-m2"
    const val checkstyle   = "8.44"
    const val jacoco       = "0.8.7"

    // Testing
    const val junitJupiter = "5.8.0-M1"
    const val assertj      = "3.20.2"
    const val mockito      = "3.11.2"

    // Logging
    const val slf4j        = "2.0.0-alpha2"
}

/**
 * Project dependency reference.
 */
object Dependencies {
    // Testing
    const val junitJupiterApi     = "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiter}"
    const val junitJupiterEngine  = "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiter}"
    const val junitJupiterParams  = "org.junit.jupiter:junit-jupiter-params:${Versions.junitJupiter}"
    const val assertjCore         = "org.assertj:assertj-core:${Versions.assertj}"
    const val mockitoInline       = "org.mockito:mockito-inline:${Versions.mockito}"
    const val mockitoJunitJupiter = "org.mockito:mockito-junit-jupiter:${Versions.mockito}"

    // Logging
    const val slf4jApi            = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val slf4jSimpleLogger   = "org.slf4j:slf4j-simple:${Versions.slf4j}"
}
