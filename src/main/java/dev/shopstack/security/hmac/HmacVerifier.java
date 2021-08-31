package dev.shopstack.security.hmac;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.function.BiFunction;

import static dev.shopstack.security.hmac.Encoding.BASE64;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Verifies a SHA-256 based HMAC (hash-based message authentication code).
 */
@Slf4j
public final class HmacVerifier implements BiFunction<String, String, Boolean> {

    private final HmacGenerator generator;

    public HmacVerifier(final String secret) {
        this.generator = new HmacGenerator(secret, BASE64);
    }

    public HmacVerifier(final String secret, final Encoding encoding)  {
        this.generator = new HmacGenerator(secret, encoding);
    }

    /**
     * Verify the provided HMAC by computing a new HMAC code and performing a comparison.
     */
    @Override
    public Boolean apply(final String hmac, final String content) {
        String newHmac = generator.apply(content);
        return MessageDigest.isEqual(hmac.getBytes(UTF_8), newHmac.getBytes(UTF_8));
    }

}

