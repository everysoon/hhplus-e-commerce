package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "유저", description = "유저 조회")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping
	public ResponseEntity<ResponseApi<List<Long>>> getAll() {
		List<Long> userIds = userService.getAll().stream().map(User::getId).toList();
		return ResponseEntity.ok(ResponseApi.of(userIds));
	}
}
