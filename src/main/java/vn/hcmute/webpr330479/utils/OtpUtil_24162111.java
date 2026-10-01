package vn.hcmute.webpr330479.utils;

import java.security.SecureRandom;

public final class OtpUtil_24162111 {
    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpUtil_24162111() {}

    public static String generate() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
