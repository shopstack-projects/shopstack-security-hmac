package dev.shopstack.security.hmac.exception;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Exception used when an operation to generate a HMAC fails.
 */
@NoArgsConstructor(access = PRIVATE)
public final class HmacGenerationFailureException extends RuntimeException {

    public HmacGenerationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
