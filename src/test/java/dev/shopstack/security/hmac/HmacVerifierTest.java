package dev.shopstack.security.hmac;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.IntStream;

import static dev.shopstack.security.test.util.RandomStringUtils.randomAlphaNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test suite for {@link HmacVerifier}.
 */
@Slf4j
public final class HmacVerifierTest {

    private HmacVerifier verifier;
    private HmacGenerator generator;

    @BeforeEach
    void beforeEach() {
        String secret = generateSecret();

        verifier = new HmacVerifier(secret);
        generator = new HmacGenerator(secret);
    }

    @Test
    void apply_whenContentIsValid_thenExpectSuccess() {
        String content = generateContent();

        String hmac = generator.apply(content);
        log.info("Generated HMAC: {}", hmac);
        assertThat(hmac).isNotBlank();

        boolean result = verifier.apply(hmac, content);
        assertThat(result).isTrue();
    }

    @Test
    void apply_whenContentHasChanged_thenExpectFailure() {
        String content = generateContent();

        String hmac = generator.apply(content);
        log.info("Generated HMAC: {}", hmac);
        assertThat(hmac).isNotBlank();

        String newContent = generateContent();
        assertThat(content).isNotEqualTo(newContent);

        boolean result = verifier.apply(hmac, newContent);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void apply_whenHmacIsNull_thenExpectException(String hmac) {
        String content = generateContent();

        assertThatThrownBy(() -> verifier.apply(hmac, content))
            .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullSource
    void apply_whenContentIsNull_thenExpectException(String content) {
        String hmac = generator.apply(generateContent());

        assertThatThrownBy(() -> verifier.apply(hmac, content))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void apply_givenMultipleSequentialCalls_whenContentIsValid_thenExpectSuccess() {
        IntStream.rangeClosed(1, 3).forEach(i -> {
            String content = generateContent();

            String hmac = generator.apply(content);
            log.info("Generated HMAC {}: {}", i, hmac);
            assertThat(hmac).isNotBlank();

            boolean result = verifier.apply(hmac, content);
            assertThat(result).isTrue();
        });
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
        return "{ \"test\": \"" + randomAlphaNumeric(10) + "\" }";
    }

}
