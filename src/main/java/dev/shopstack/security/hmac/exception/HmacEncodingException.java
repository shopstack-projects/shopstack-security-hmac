package dev.shopstack.security.hmac.exception;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Exception thrown when a problem occurs when encoding a HMAC.
 */
@NoArgsConstructor(access = PRIVATE)
public final class HmacEncodingException extends RuntimeException {

    public HmacEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

}
