package kr.hhplus.be.server;

import kr.hhplus.be.server.config.BaseE2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class E2ETestV1 extends BaseE2ETest {

    @Test
    @DisplayName("[200] 유저 포인트 조회")
    public void getUserPoint() {
        Long userId = 1L;

        get("/users/{userId}/point",userId)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("message",equalTo("요청이 성공했습니다."))
                .body("processCode",equalTo("1"))
                .body("data",notNullValue());

    }
}
