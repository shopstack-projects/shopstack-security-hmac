package dev.shopstack.security.hmac;

import dev.shopstack.security.hmac.exception.HmacGenerationFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Verifies a SHA-256 cryptographic HMAC (hash-based message authentication code).
 */
@Slf4j
@RequiredArgsConstructor
public final class HmacGenerator implements Function<String, String> {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final String secret;

    /**
     * Generate a HMAC code for the provided content.
     *
     * @throws HmacGenerationFailureException if a message authentication code cannot be computed.
     */
    @Override
    public String apply(final String content) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);

            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(UTF_8), HMAC_SHA256);
            mac.init(keySpec);

            byte[] macData = mac.doFinal(content.getBytes(UTF_8));
            return Base64.getEncoder().encodeToString(macData);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Unable to compute an authentication code.", e);
            throw new HmacGenerationFailureException("Unable to compute an authentication code.", e);
        }
    }

}

