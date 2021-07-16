package dev.shopstack.security.hmac;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.function.BiFunction;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Provides methods to generate and verify HMACs (hash-based message authentication codes).
 */
@Slf4j
@Getter
public class HmacVerifier implements BiFunction<String, String, Boolean> {

    private final String secret;

    private final HmacGenerator generator;

    public HmacVerifier(String secret) {
        this.secret = secret;
        this.generator = new HmacGenerator(secret);
    }

    /**
     * Verify the provided HMAC by computing a new HMAC code for comparison.
     */
    @Override
    public Boolean apply(final String hmac, final String content) {
        String newHmac = generator.apply(content);
        return MessageDigest.isEqual(hmac.getBytes(UTF_8), newHmac.getBytes(UTF_8));
    }

}

