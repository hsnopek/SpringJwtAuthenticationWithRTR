package hr.hsnopek.springjwtrtr.security.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.util.Assert;

import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;

public class SecurityUtils {
	private static final String APP_NAME = "JWTRTR";

    /**
     * Generates url for scanning of QR code.
     * 
     * @param username username
     * @param secret OTP secret
     * 
     * @return {@link String}
     */
	public static String generateQrUrl(String username, String secret) {
		Assert.notNull(secret, "User must have a secret");

		String url = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, username,
				secret, APP_NAME);

		try {
			return  URLEncoder.encode(url, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("Failed to encode QR URL", ex);
		}
	}

    /**
     * Verifies authenticator generated OTP against {@link User } secret.
     * 
     * @param user user
     * @param code authenticator generated OTP
     * 
     * @return {@link boolean}
     */
	public static boolean verifyOTP(User user, String code) {
		Assert.notNull(user.getSecret(), "User must have a secret");

		Totp totp = new Totp(user.getSecret());
		try {
			return totp.verify(code);
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}