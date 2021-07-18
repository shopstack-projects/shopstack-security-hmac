package dev.shopstack.security.test;

import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PRIVATE;

/**
 * Utility class for generating random strings.
 */
@NoArgsConstructor(access = PRIVATE)
public final class RandomStringUtils {

    private static final String SALT_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Return a random alphanumeric string of the specified length.
     */
    public static String randomAlphaNumeric(int length) {
        StringBuilder salt = new StringBuilder();
        Random random = new Random();

        IntStream.range(0, length).forEach(i -> {
            int index = (int) (random.nextFloat() * SALT_CHARS.length());
            salt.append(SALT_CHARS.charAt(index));
        });

        return salt.toString();
    }

}
