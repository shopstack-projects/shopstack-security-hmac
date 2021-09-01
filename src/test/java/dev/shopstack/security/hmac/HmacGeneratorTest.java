package dev.shopstack.security.hmac;

import dev.shopstack.security.hmac.exception.HmacEncodingException;
import dev.shopstack.security.hmac.exception.HmacGeneratorInitializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

import static dev.shopstack.security.test.util.RandomStringUtils.randomAlphaNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;

/**
 * Test suite for {@link HmacGenerator}.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public final class HmacGeneratorTest {

    /**
     * Test cases for {@link HmacGenerator#HmacGenerator(String)}.
     */
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
        void constructor_whenMacInitializedWithBadKeySpec_thenExpectException() throws Exception {
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

    /**
     * Test cases for {@link HmacGenerator#apply(String)}.
     */
    @Nested
    class Apply {

        private HmacGenerator generator;

        @BeforeEach
        void beforeEach() {
            generator = spy(new HmacGenerator(generateSecret()));
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

            assertThat(true).isTrue(); // Passes PMD checks.
        }

        @ParameterizedTest
        @EnumSource(Encoding.class)
        void apply_givenDifferentEncodings_thenExpectSuccess(Encoding encoding) {
            generator = new HmacGenerator(generateSecret(), encoding);
            String content = generateContent();

            String hmac = generator.apply(content);
            log.info("Generated HMAC: {}", hmac);
            assertThat(hmac).isNotBlank();
        }

        @ParameterizedTest
        @EnumSource(Encoding.class)
        void apply_whenEncodingFails_thenExpectException(Encoding encoding) throws Exception {
            generator = new HmacGenerator(generateSecret(), encoding);

            // Overwrite the generator's internal encoder.
            Field encoderField = generator.getClass().getDeclaredField("encoder");
            encoderField.setAccessible(true);

            BinaryEncoder encoder = mock(BinaryEncoder.class);
            encoderField.set(generator, encoder);

            doThrow(new EncoderException()).when(encoder).encode(any());

            assertThatThrownBy(() -> generator.apply(generateContent()))
                .isInstanceOf(HmacEncodingException.class);
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
