package dev.shopstack.security.hmac;

import dev.shopstack.security.hmac.exception.HmacEncodingException;
import dev.shopstack.security.hmac.exception.HmacGeneratorInitializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base16;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

import static dev.shopstack.security.hmac.Encoding.BASE16;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Generates a SHA-256 based HMAC (hash-based message authentication code).
 */
@Slf4j
public final class HmacGenerator implements Function<String, String> {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final Mac mac;
    private final BinaryEncoder encoder;

    public HmacGenerator(final String secret) {
        this.mac = initMac(secret);
        this.encoder = new Base64();
    }

    public HmacGenerator(final String secret, final Encoding encoding) {
        this.mac = initMac(secret);
        this.encoder = buildEncoder(encoding);
    }

    /**
     * Initialize a new {@link Mac} instance.
     *
     * @throws HmacGeneratorInitializationException if the HMAC generator cannot be initialized.
     */
    private Mac initMac(final String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);

            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(UTF_8), HMAC_SHA256);
            mac.init(keySpec);

            return mac;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Unable to initialize the HMAC generator.", e);
            throw new HmacGeneratorInitializationException("Unable to initialize the HMAC generator.", e);
        }
    }

    /**
     * Generate a new HMAC code for the provided content.
     */
    @Override
    public String apply(final String content) {
        try {
            byte[] macData = mac.doFinal(content.getBytes(UTF_8));
            return new String(encoder.encode(macData), UTF_8);
        } catch (EncoderException e) {
            log.error("Unable to encode the HMAC.", e);
            throw new HmacEncodingException("Unable to encode the HMAC.", e);
        }
    }

    /**
     * Create a {@link BinaryEncoder} object using the provided {@link Encoding}.
     */
    private BinaryEncoder buildEncoder(Encoding encoding) {
        if (encoding == BASE16) {
            return new Base16(true);
        }

        return new Base64();
    }

}

