package dev.shopstack.security;

import dev.shopstack.security.hmac.HmacGenerator;
import dev.shopstack.security.hmac.exception.HmacGeneratorInitializationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

import static dev.shopstack.security.test.RandomStringUtils.randomAlphaNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

/**
 * Test suite for {@link HmacGenerator}.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public final class HmacGeneratorTest {

    @Nested
    class Constructor {

        @Test
        void constructor_whenMacAlgorithmDoesNotExist_thenExpectException() {
            try (MockedStatic<Mac> mac = mockStatic(Mac.class)) {
                mac.when(() -> Mac.getInstance("HmacSHA256"))
                    .thenThrow(new NoSuchAlgorithmException());

                assertThatThrownBy(() -> new HmacGenerator(generateSecret()))
                    .isInstanceOf(HmacGeneratorInitializationException.class);
            }
        }

        @Test
        void constructor_whenMacInitializedWithBadKeySpec_thenExpectException() throws InvalidKeyException {
            try (MockedStatic<Mac> mac = mockStatic(Mac.class)) {
                Mac macInstance = mock(Mac.class);

                mac.when(() -> Mac.getInstance(anyString()))
                    .thenReturn(macInstance);

                doThrow(new InvalidKeyException())
                    .when(macInstance).init(any(SecretKeySpec.class));

                assertThatThrownBy(() -> new HmacGenerator(generateSecret()))
                    .isInstanceOf(HmacGeneratorInitializationException.class);
            }
        }

    }

    @Nested
    class Apply {

        private HmacGenerator generator;

        @BeforeEach
        void beforeEach() {
            generator = new HmacGenerator(generateSecret());
        }

        @Test
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

        @Test
        void apply_givenMultipleSequentialCalls_whenContentIsValid_thenExpectSuccess() {
            IntStream.rangeClosed(1, 3).forEach(i -> {
                String content = generateContent();

                String hmac = generator.apply(content);
                log.info("Generated HMAC {}: {}", i, hmac);
                assertThat(hmac).isNotBlank();
            });
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
        return "{ \"test: \"" + randomAlphaNumeric(10) + "\" }";
    }

}
