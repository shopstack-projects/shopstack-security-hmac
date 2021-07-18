package dev.shopstack.security;

import dev.shopstack.security.hmac.HmacGenerator;
import dev.shopstack.security.hmac.HmacVerifier;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static dev.shopstack.security.test.RandomStringUtils.randomAlphaNumeric;
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

    @RepeatedTest(3)
    void apply_whenContentIsValid_thenExpectSuccess() {
        String content = generateContent();

        String hmac = generator.apply(content);
        log.info("Generated HMAC: {}", hmac);
        assertThat(hmac).isNotBlank();

        boolean result = verifier.apply(hmac, content);
        assertThat(result).isTrue();
    }

    @RepeatedTest(3)
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
