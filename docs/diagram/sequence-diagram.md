sequenceDiagram
        
    participant PointController
    participant PointService
    participant PointRepository
    alt 잔액 충전
    PointController ->> PointService: 1. 잔액 충전 요청 들어옴
    PointService ->> PointRepository: 2. 잔액 업데이트
    PointRepository -->> PointService : 3. 잔액 업데이트 완료 응답
    PointService -->> PointController : 4. 충전 성공 응답
    else 잔액 조회
    PointController ->> PointService: 1. 잔액 조회 요청
    PointService ->> PointRepository: 2. UserId 기반으로 잔액 조회
    PointRepository -->> PointService : 3. 잔액 정보 반환
    PointService -->> PointController : 4. 잔액 조회 성공 응답 
    end
    
sequenceDiagram
        
    participant ProductController
    participant ProductService
    participant OrderService
    alt 상품 조회
    ProductController ->> ProductService: 1.  상품 목록 조회 요청
    ProductService -->> ProductController: 3. 상품 목록 조회 반환 
    else 인기 판매 상품 조회
    ProductController ->> ProductService: 1.  인기 상품 조회 요청
    ProductService ->> OrderService: 2. 최근 3일간 판매 데이터 조회
    OrderService -->> ProductService: 3. 상위 5개 상품 반환
    ProductService -->> ProductController: 4. 인기 상품 목록 반환
    end
    
sequenceDiagram 
    participant UserService
    participant CouponController
    participant CouponService
    participant CouponRepository
    
    alt 쿠폰 발급
    CouponController ->> CouponService : 쿠폰 발급 요청
    CouponService ->> CouponService : 쿠폰 재고 확인 및 발급
    CouponService -->> CouponController : 쿠폰 발급 성공/실패 결과 반환 
    else 보유 쿠폰 조회
    UserService ->> CouponService : 보유 쿠폰 목록 조회 요청
    CouponService ->> CouponRepository : userId 기반 보유 쿠폰 목록 조회
    CouponRepository -->> CouponService : 보유 쿠폰 목록 반환 
    CouponService -->> UserService : 보유 쿠폰 목록 반환 
    end
   
    
