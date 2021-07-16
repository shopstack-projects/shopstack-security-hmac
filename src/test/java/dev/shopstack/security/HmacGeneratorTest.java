package dev.shopstack.security;

import dev.shopstack.security.hmac.HmacGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static dev.shopstack.security.test.RandomStringGenerator.randomAlphaNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test suite for {@link HmacGenerator}.
 */
@Slf4j
public class HmacGeneratorTest {

    private HmacGenerator generator;

    @BeforeEach
    void beforeEach() {
        generator = new HmacGenerator(generateSecret());
    }

    @RepeatedTest(3)
    void apply_whenContentIsValid_thenExpectSuccess() {
        String content = generateContent();

        String hmac = generator.apply(content);
        log.info("Generated HMAC: {}", hmac);
        assertThat(hmac).isNotBlank();
    }

    @ParameterizedTest
    @EmptySource
    void apply_whenContentIsEmpty_thenExpectSuccess(String content) {
        String hmac = generator.apply(content);
        log.info("Generated HMAC: {}", hmac);
        assertThat(hmac).isNotBlank();
    }

    @ParameterizedTest
    @NullSource
    void apply_whenContentIsNull_thenExpectException(String content) {
        assertThatThrownBy(() -> generator.apply(content))
            .isInstanceOf(NullPointerException.class);
    }

    /* Fixtures */

    /**
     * Generate a random secret.
     */
    private String generateSecret() {
        final String secretPrefix = "shpss_";
        return secretPrefix + randomAlphaNumeric(32);
    }

    /**
     * Generate random content.
     */
    private String generateContent() {
        return "{ \"test: \"" + randomAlphaNumeric(10) + "\" }";
    }

}
