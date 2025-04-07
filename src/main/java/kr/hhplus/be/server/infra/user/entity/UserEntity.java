package kr.hhplus.be.server.infra.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private BigDecimal point = BigDecimal.ZERO;

    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    /**
     * address, city, zipcode, detailedAddress의 Address 객체로 보완해도 좋음
     * */
    private String address;
    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
