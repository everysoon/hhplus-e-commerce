package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	private String name;
	@Column(nullable = false, unique = true)
	private String email;
	/**
	 * address, city, zipcode, detailedAddress의 Address 객체로 보완해도 좋음
	 */
	private String address;
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;
}
