package dev.shopstack.security.hmac;

import dev.shopstack.security.hmac.exception.HmacGeneratorInitializationException;
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
public final class HmacGenerator implements Function<String, String> {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final Mac mac;

    /**
     * Create a new {@link HmacGenerator} instance.
     *
     * @throws HmacGeneratorInitializationException if the HMAC generator cannot be initialized.
     */
    public HmacGenerator(final String secret) {
        try {
            this.mac = Mac.getInstance(HMAC_SHA256);

            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(UTF_8), HMAC_SHA256);
            mac.init(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Unable to initialize the HMAC generator.", e);
            throw new HmacGeneratorInitializationException("Unable to initialize the HMAC generator.", e);
        }
    }

    /**
     * Generate a HMAC code for the provided content.
     */
    @Override
    public String apply(final String content) {
        byte[] macData = mac.doFinal(content.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(macData);
    }

}

