package dev.shopstack.security.hmac.exception;

import dev.shopstack.security.hmac.HmacGenerator;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Exception used when a {@link HmacGenerator} instance cannot be initialized.
 */
@NoArgsConstructor(access = PRIVATE)
public final class HmacGeneratorInitializationException extends RuntimeException {

    public HmacGeneratorInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
