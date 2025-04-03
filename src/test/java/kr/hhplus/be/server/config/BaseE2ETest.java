package kr.hhplus.be.server.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

/**
 * E2E Test
 * 1. 실제 API 호출 후 데이터 흐름을 검증하는 테스트
 * 2. 애플리케이션의 전체적인 동작을 검증 (비지니스 로직,DB,컨트롤러 포함)
 * 3. 단위테스트나 통합테스트보다 사용자의 실제 요청을 시뮬레이션
 * 4. API 요청 -> DB조회/수정 -> 응답반환 전체 흐름을 검증
 */

//@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseE2ETest {

	@LocalServerPort
	protected int port;

//	@Container
//	private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
//		.withDatabaseName("hhplus")
//		.withUsername("soon")
//		.withPassword("alstjsl1!");

	@BeforeAll
	public void setup() {
		// RestAssured 설정 (테스트 대상 API의 base URI 및 포트 설정)
		RestAssured.baseURI = "http://localhost/api";
		RestAssured.port = port;
	}
//	@DynamicPropertySource
//	static void configureProperties(DynamicPropertyRegistry registry) {
//		MYSQL_CONTAINER.start();
//		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
//		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
//		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
//		registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);
//	}
//	@PreDestroy
//	public void stopContainer() {
//		if (MYSQL_CONTAINER.isRunning()) {
//			MYSQL_CONTAINER.stop();
//		}
//	}
}
