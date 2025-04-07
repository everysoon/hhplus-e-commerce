package kr.hhplus.be.server.domain.order;

public enum OrderStatus {
    ORDERED,
    SHIPPED, // 배송됨
    DELIVERED, // 배송중
    CANCELED
}
