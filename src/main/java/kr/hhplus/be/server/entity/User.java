package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.dto.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
	@Builder.Default
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
