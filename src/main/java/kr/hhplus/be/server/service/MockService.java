package kr.hhplus.be.server.service;

import kr.hhplus.be.server.dto.order.OrderItemDTO;
import kr.hhplus.be.server.dto.order.OrderRequestDTO;
import kr.hhplus.be.server.dto.order.OrderResponseDTO;
import kr.hhplus.be.server.dto.product.ProductResponseDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.dto.user.UserResponseDTO;
import kr.hhplus.be.server.entity.*;
import kr.hhplus.be.server.enums.Category;
import kr.hhplus.be.server.enums.CouponStatus;
import kr.hhplus.be.server.enums.CouponType;
import kr.hhplus.be.server.enums.ProductStatus;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class MockService {

    private final List<Product> products = new ArrayList<>();
    private final List<Coupon> coupons = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    int stock = 10;

    // 인기 상품 조회
    private List<ProductResponseDTO> findAllPopularProducts() {
        List<Product> products = new ArrayList<>();
        products.add(createProduct(1L,10));
        products.add(createProduct(2L,10));
        products.add(createProduct(3L,10));
        return products.stream().map(Product::toResponseDTO).collect(Collectors.toList());
    }

    // 상품 조회
    private ProductResponseDTO findProductById(Long productId) {
        return createProduct(productId,10).toResponseDTO();
    }

    // 유저 포인트 충전
    private UserResponseDTO chargePoint(Long userId, BigDecimal price) {
        User user = createUser(userId);
        user.charge(price);
        return user.toResponseDTO();
    }

    // 유저 포인트 조회
    private UserResponseDTO getUserPoint(Long userId) {
        return createUser(userId).toResponseDTO();
    }

    // 선착순 쿠폰 등록
    private UserCouponResponseDTO issueCoupon(Long userId) {
        User user = createUser(userId);
        Coupon coupon = createCoupon();
        return createUserCoupon(user, coupon, CouponStatus.ISSUED).toResponse();
    }

    // 유저 보유 쿠폰 조회
    private UserCouponResponseDTO getUserCoupon(Long userId) {
        User user = createUser(userId);
        Coupon coupon = createCoupon();
        return createUserCoupon(user, coupon, CouponStatus.USED).toResponse();
    }

    // 주문/결제
//    private List<OrderResponseDTO> order(OrderRequestDTO dto) {
//        User user =  createUser(dto.getUserId());
//        List<Coupon> coupon = new ArrayList<>();
//        coupon.add(createCoupon());
//        List<Product> products = dto.getProducts().stream().map(opr->createProduct(opr.getProductId(),1)).toList();
//
//        return OrderResponseDTO.builder()
//                .userId(user.getId())
//                .orderInfo()
//                .build();
//    }

    // utils
//    private OrderItem createOrderItem(Product product,List<Coupon> coupon){
//        return OrderItem.builder()
//                .product(product)
//                .coupon(coupons)
//                .couponDiscountAmount()
//                .build();
//    }

    private UserCoupon createUserCoupon(User user, Coupon coupon, CouponStatus status) {
        return UserCoupon.builder()
                .coupon(coupon)
                .id(1L)
                .user(user)
                .status(status)
                .remainingStock(stock--)
                .issuedAt(LocalDateTime.now())
                .build();
    }
    private Coupon createCoupon() {
        return Coupon.builder()
                .id(UUID.randomUUID())
                .type(CouponType.FIXED)
                .description("TEST COUPON")
                .expiredAt(LocalDateTime.now().plusDays(7L))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private User createUser(Long userId) {
        return User.builder()
                .id(userId)
                .point(convertToBigDecimal(0))
                .name("minsoon")
                .email("soonforjoy@gmail.com")
                .address("Guro,Seoul")
                .password("$2a$12$NooMM5e1WBiD8uYqRkblTuNN0iesou/beJ/EeuTofsUmzjhuZ6NgK")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Product createProduct(Long productId,Integer stock) {
        return Product.builder()
                .id(productId)
                .stock(stock)
                .price(convertToBigDecimal(5000))
                .productName("다이소 장난감 " + productId)
                .category(Category.BABY)
                .description("유아용 다이소 자동차 장난감 " + productId)
                .status(ProductStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private BigDecimal convertToBigDecimal(Integer number) {
        return BigDecimal.valueOf(number);
    }
}
