package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
	private final Long id;
	private BigDecimal point;
	private final String email;
	private final String address;
	private final String name;
	private final String password;
	private final LocalDateTime createdAt;

	public void charge(BigDecimal price){
		this.point = this.point.add(price);
	}
	public void use(BigDecimal price){
		this.point = this.point.subtract(price);
	}

	public UserResponseDTO toResponseDTO(){
		return UserResponseDTO.builder()
			.id(id)
			.point(point)
			.name(name)
			.email(email)
			.address(address)
			.build();
	}
}
