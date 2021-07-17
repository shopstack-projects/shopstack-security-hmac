object Versions {
    const val lombokPlugin = "6.0.0-m2"

    // Testing
    const val junitJupiter = "5.8.0-M1"
    const val assertj      = "3.20.2"

    // Logging
    const val slf4j        = "2.0.0-alpha2"
}

object Dependencies {
    // Testing
    const val junitJupiterApi    = "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiter}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiter}"
    const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junitJupiter}"
    const val assertjCore        = "org.assertj:assertj-core:${Versions.assertj}"

    // Logging
    const val slf4jApi           = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val slf4jSimpleLogger  = "org.slf4j:slf4j-simple:${Versions.slf4j}"
}
