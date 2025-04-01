package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@Tag(name="잔액 관리",description = "잔액 충전 및 조회 API")
public class PointController {
}
