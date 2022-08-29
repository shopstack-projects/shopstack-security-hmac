/**
 * Version reference for project dependencies.
 */
object Versions {
    const val lombokPlugin = "6.5.0.3"
    const val checkstyle   = "10.3.2"
    const val jacoco       = "0.8.8"

    const val commonsCodec = "1.15"

    // Logging
    const val slf4j        = "2.0.0"

    // Testing
    const val junitJupiter = "5.9.0"
    const val assertj      = "3.23.1"
    const val mockito      = "4.7.0"
}

/**
 * Project dependency reference.
 */
object Dependencies {
    const val apacheCommonsCodec  = "commons-codec:commons-codec:${Versions.commonsCodec}"

    // Logging
    const val slf4jApi            = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val slf4jSimpleLogger   = "org.slf4j:slf4j-simple:${Versions.slf4j}"

    // Testing
    const val junitJupiterApi     = "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiter}"
    const val junitJupiterEngine  = "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiter}"
    const val junitJupiterParams  = "org.junit.jupiter:junit-jupiter-params:${Versions.junitJupiter}"
    const val assertjCore         = "org.assertj:assertj-core:${Versions.assertj}"
    const val mockitoInline       = "org.mockito:mockito-inline:${Versions.mockito}"
    const val mockitoJunitJupiter = "org.mockito:mockito-junit-jupiter:${Versions.mockito}"
}
