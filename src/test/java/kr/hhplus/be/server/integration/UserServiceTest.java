package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseIntegrationTest {
	@Autowired
	private UserRepository userRepository;
	private UserService userService;
	@BeforeAll
	void setup() {
		userService = new UserService(userRepository);
	}
//	@Test
//	void 유저_정보_조회_정상(){
//		/**
//		 * userId :1
//		 * address : 713 Mickey Drive, Diamouth, FL 18534
//		 * email : vashti.nienow@hotmail.com
//		 * name : Rusty Schroeder IV
//		 */
//		//given
//		Long userId = 1L;
//		//when
//		User user = userService.get(userId);
//		//then
//		assertAll("유저 상세 조회 정보 체크",
//			() -> assertThat(user.getEmail()).isEqualTo("vashti.nienow@hotmail.com"),
//			() -> assertThat(user.getName()).isEqualTo("Rusty Schroeder IV"),
//			() -> assertThat(user.getAddress()).isEqualTo("713 Mickey Drive, Diamouth, FL 18534")
//		);
//	}
}
