package kr.hhplus.be.server.application.user;

import java.math.BigDecimal;
import kr.hhplus.be.server.application.user.dto.UserCouponResponseDTO;
import kr.hhplus.be.server.application.user.dto.UserOrderResponseDTO;
import kr.hhplus.be.server.application.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	public UserResponseDTO getUserPoint(Long userId){
		return null;
	}
	public UserCouponResponseDTO getUserCoupon(Long userId){
		return null;
	}
	public UserCouponResponseDTO issueCoupon(Long userId){
		return null;
	}
	public UserResponseDTO chargePoint(Long userId, BigDecimal price){
		return null;
	}
	public UserResponseDTO usePoint(Long userId, BigDecimal price){
		return null;
	}
	public UserOrderResponseDTO getOrders(Long userId){
		return null;
	}
}
