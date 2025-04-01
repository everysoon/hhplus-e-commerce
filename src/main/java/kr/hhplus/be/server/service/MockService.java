package kr.hhplus.be.server.service;

import kr.hhplus.be.server.ResponseApi;
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
import kr.hhplus.be.server.enums.OrderStatus;
import kr.hhplus.be.server.enums.PaymentMethod;
import kr.hhplus.be.server.enums.PaymentStatus;
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

    int stock = 10;

    // 인기 상품 조회
    public ResponseApi<List<ProductResponseDTO>> findAllPopularProducts() {
        List<Product> products = new ArrayList<>();
        products.add(createProduct(1L, 10));
        products.add(createProduct(2L, 10));
        products.add(createProduct(3L, 10));
        List<ProductResponseDTO> result = products.stream().map(Product::toResponseDTO)
            .collect(Collectors.toList());
        return new ResponseApi<>(result);
    }

    // 상품 조회
    public ResponseApi<ProductResponseDTO> findProductById(Long productId) {
        ProductResponseDTO result = createProduct(productId, 10).toResponseDTO();
        return new ResponseApi<>(result);
    }

    // 유저 포인트 충전
    public ResponseApi<UserResponseDTO> chargePoint(Long userId, BigDecimal price) {
        User user = createUser(userId);
        user.charge(price);
        return new ResponseApi<>(user.toResponseDTO());
    }

    // 유저 포인트 조회
    public ResponseApi<UserResponseDTO> getUserPoint(Long userId) {
        return new ResponseApi<>(createUser(userId).toResponseDTO());
    }

    // 선착순 쿠폰 등록
    public ResponseApi<UserCouponResponseDTO> issueCoupon(Long userId) {
        User user = createUser(userId);
        Coupon coupon = createCoupon();
        UserCouponResponseDTO result = createUserCoupon(user, coupon,
            CouponStatus.ISSUED).toResponse();
        return new ResponseApi<>(result);
    }

    // 유저 보유 쿠폰 조회
    public ResponseApi<UserCouponResponseDTO> getUserCoupon(Long userId) {
        User user = createUser(userId);
        Coupon coupon = createCoupon();
        UserCouponResponseDTO result = createUserCoupon(user, coupon,
            CouponStatus.USED).toResponse();
        return new ResponseApi<>(result);
    }

    // 주문/결제
    public ResponseApi<OrderResponseDTO> order(OrderRequestDTO dto) {
        User user = createUser(dto.getUserId());
        List<Coupon> coupons = createCouponList();
        List<OrderItem> orderItems = dto.getProducts().stream()
            .map(opr -> createProduct(opr.getProductId(), 1))
            .map(p -> createOrderItem(p, 10))
            .toList();

        Payment payment = createDefaultPayment();
        Order order = createOrder(payment);
        List<OrderItemDTO> orderItemDtos = orderItems.stream().map(o -> o.toDTO(order.getId())).toList();

        OrderResponseDTO result = OrderResponseDTO.builder()
            .userId(user.getId())
            .paymentMethod(payment.getPaymentMethod())
            .paymentStatus(PaymentStatus.COMPLETED)
            .totalPrice(order.getTotalPrice())
            .couponDiscountAmount(order.getTotalDiscount())
            .orderInfo(orderItemDtos)
            .orderedAt(order.getCreatedAt())
            .status(OrderStatus.ORDERED)
            .build();
        return new ResponseApi<>(result);
    }

    // utils
    private List<Coupon> createCouponList(){
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(createCoupon());
        return coupons;
    }
    private Order createOrder(Payment payment) {
        return Order.builder()
            .payment(payment)
            .build();
    }
    private Payment createDefaultPayment() {
        return Payment.builder()
            .paymentMethod(PaymentMethod.POINTS)
            .createdAt(LocalDateTime.now())
            .build();
    }
    private OrderItem createOrderItem(Product product, Integer quantity) {
        return OrderItem.builder()
            .product(product)
            .quantity(quantity)
            .build();
    }

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

    private Product createProduct(Long productId, Integer stock) {
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
