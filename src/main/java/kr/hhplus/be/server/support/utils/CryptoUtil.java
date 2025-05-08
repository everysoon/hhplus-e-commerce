package kr.hhplus.be.server.support.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {
	public static String sha256Hex(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			// 바이트 배열을 Hex 문자열로 변환
			return bytesToHex(hash);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 알고리즘이 존재하지 않습니다.", e);
		}
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();

		for (byte b : bytes) {
			// 바이트를 unsigned int로 바꾸고 16진수로 변환
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0'); // 1자리면 앞에 0 붙이기
			}
			hexString.append(hex);
		}

		return hexString.toString();
	}
}
