@startuml

actor User
actor Admin

User --> (잔액 충전)
User --> (잔액 조회)
User --> (상품 조회)
User --> (주문/결제)
User --> (선착순 쿠폰 발급)
User --> (보유 쿠폰 목록 조회)
User --> (인기 판매 상품 조회)

Admin --> (쿠폰 재고 관리)
Admin --> (상품 재고 관리)
Admin --> (주문 처리)

(잔액 충전) ..> (PointService) : 호출
(잔액 조회) ..> (PointService) : 호출
(상품 조회) ..> (ProductService) : 호출
(주문/결제) ..> (OrderService) : 호출
(주문/결제) ..> (PointService) : 호출
(선착순 쿠폰 발급) ..> (CouponService) : 호출
(보유 쿠폰 목록 조회) ..> (CouponService) : 호출
(인기 판매 상품 조회) ..> (OrderService) : 호출

(쿠폰 재고 관리) ..> (CouponService) : 호출
(상품 재고 관리) ..> (ProductService) : 호출
(주문 처리) ..> (OrderHistoryService) : 호출

@enduml
