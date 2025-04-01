package kr.hhplus.be.server.dto.order;

import kr.hhplus.be.server.enums.OrderStatus;
import kr.hhplus.be.server.enums.PaymentMethod;
import kr.hhplus.be.server.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long userId;
    private List<OrderItemDTO> orderInfo;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal totalPrice;
    private BigDecimal couponDiscountAmount;
    private LocalDateTime orderedAt;
    private OrderStatus status;
}
