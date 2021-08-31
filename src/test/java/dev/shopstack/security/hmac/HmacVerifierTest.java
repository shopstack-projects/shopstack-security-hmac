package dev.shopstack.security.hmac;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.IntStream;

import static dev.shopstack.security.hmac.Encoding.BASE16;
import static dev.shopstack.security.hmac.Encoding.BASE64;
import static dev.shopstack.security.test.util.RandomStringUtils.randomAlphaNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test suite for {@link HmacVerifier}.
 */
@Slf4j
public final class HmacVerifierTest {

    /**
     * Test cases for {@link HmacVerifier#apply(String, String)} when using {@link Encoding#BASE64}.
     */
    @Nested
    class ApplyUsingBase64Encoding {

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

            assertThat(true).isTrue(); // Passes PMD checks.
        }

        @Test
        void apply_whenHmacIsBase16Encoded_thenExpectFailure() {
            String secret = generateSecret();

            HmacGenerator generator = new HmacGenerator(secret, BASE16);
            HmacVerifier verifier = new HmacVerifier(secret, BASE64);

            String content = generateContent();

            String hmac = generator.apply(content); // Generate Base16
            log.info("Generated HMAC: {}", hmac);
            assertThat(hmac).isNotBlank();

            boolean result = verifier.apply(hmac, content); // Verify Base64
            assertThat(result).isFalse();
        }

    }

    /**
     * Test cases for {@link HmacVerifier#apply(String, String)} when using {@link Encoding#BASE16}.
     */
    @Nested
    class ApplyUsingBase16Encoding {

        private HmacVerifier verifier;
        private HmacGenerator generator;

        @BeforeEach
        void beforeEach() {
            String secret = generateSecret();

            verifier = new HmacVerifier(secret, BASE16);
            generator = new HmacGenerator(secret, BASE16);
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
        void apply_whenHmacIsBase64Encoded_thenExpectFailure() {
            String secret = generateSecret();

            HmacGenerator generator = new HmacGenerator(secret, BASE64);
            HmacVerifier verifier = new HmacVerifier(secret, BASE16);

            String content = generateContent();

            String hmac = generator.apply(content); // Generate Base64
            log.info("Generated HMAC: {}", hmac);
            assertThat(hmac).isNotBlank();

            boolean result = verifier.apply(hmac, content); // Verify Base16
            assertThat(result).isFalse();
        }

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
