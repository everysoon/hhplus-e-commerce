package kr.hhplus.be.server.support.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	// 객체 -> JSON 문자열
	public static String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("JSON 직렬화 실패", e);
		}
	}

	// JSON 문자열 -> 객체
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("JSON 역직렬화 실패", e);
		}
	}
}
