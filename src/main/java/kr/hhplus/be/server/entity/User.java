package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    private Float point;

    private String name;
    private String email;
    /**
     * address, city, zipcode, detailedAddress의 Address 객체로 보완해도 좋음
     * */
    private String address;
    private String phone;
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;
}
