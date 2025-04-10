package kr.hhplus.be.server.application.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record OrderResponseDTO (
	Long userId,
	Long orderId,
	List<OrderItemResponse> orderItems,
	PaymentMethod paymentMethod,
	PaymentStatus paymentStatus,
	BigDecimal totalPrice,
	BigDecimal couponDiscountAmount,
	LocalDateTime orderedAt,
	OrderStatus orderStatus
){

}
