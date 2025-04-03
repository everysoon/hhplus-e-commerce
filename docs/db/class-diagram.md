classDiagram
    class USER {
        long id (*)
        string name
        string email (*)
        string password (*)
        string address
        bigdecimal point
        timestamp createdAt (*)
        
        +register()
        +charge(amount: bigdecimal)
        +use(amount: bigdecimal)
    }

    class USER_COUPON {
        long id (*)
        long userId (*)
        long couponId (*)
        enum status : 'issued', 'used', 'expired', 'revoked', 'pending' (*)
        int remainingStock (*)
        timestamp issuedAt (*)
        +isValid()
    }

    class POINT_HISTORY {
        long id (*)
        long userId (*)
        enum status : 'charged', 'used' (*)
        bigdecimal amount (*)
        timestamp createdAt (*)
    }

    class ORDER {
        long id (*)
        long userId (*)
        long paymentId (*)
        bigdecimal totalPrice (*)
        bigdecimal totalDiscount (*)
        timestamp createdAt (*)
        +sumDiscountAmount(amount:bigdecimal)
        +getTotalPrice()
        +cancelOrder()
    }

    class ORDER_ITEM {
        long id (*)
        long orderId (*)
        long productId (*)
        int quantity (*)
        +updateQuantity(quantity: int)
    }

    class PRODUCT {
        long id (*)
        string category (*)
        enum status : 'available', 'out_of_stock', 'reserved' (*)
        string productName
        string description
        float price (*)
        int stock (*)
        timestamp createdAt (*)
        
        +updateStock(qty: int)
        +changeStatus(status: enum)
        +updateInfo()        
    }

    class COUPON {
        UUID id (*)
        float discount (*)
        string description
        int stock (*)
        enum type : 'percent', 'fixed' (*)
        timestamp expiredAt (*)
        timestamp createdAt (*)
        
        +issue()        
        +isExpired()
        +calculateDiscount(priceSum: bigdecimal)
    }

    class PAYMENT {
        long id (*)
        long orderId (*)
        timestamp createdAt (*)
        enum paymentMethod : 'card', 'bank_transfer', 'points', 'paypal' (*)
        
        +update()
        +refundPayment()        
    }

    class PAYMENT_HISTORY {
        long id (*)
        enum status : 'pending', 'completed', 'failed', 'refunded' (*)
        long paymentId (*)
        string description
        timestamp createdAt (*)
        bigdecimal amount 
        
        +recordTransaction()   
    }

    class ORDER_HISTORY {
        long id (*)
        long orderId (*)
        enum status : 'pending', 'shipped', 'delivered', 'canceled' (*)
        timestamp createdAt (*)
        
        +updateStatus(status: enum)
    }

    USER "1" -- "0..*" ORDER : places
    USER "1" -- "0..*" USER_COUPON : has
    USER_COUPON "0..*" -- "1" COUPON : is_assigned_to
    USER "1" -- "0..*" POINT_HISTORY : has
    ORDER "1" -- "0..*" ORDER_ITEM : contains
    ORDER "1" -- "1" PAYMENT : has
    ORDER "1" -- "0..*" ORDER_HISTORY : has
    PAYMENT "1" -- "0..*" PAYMENT_HISTORY : has
    COUPON "1" -- "0..*" ORDER : used_in
    PRODUCT "1" -- "0..*" ORDER_ITEM : used_in

    class USER_SERVICE {
        +registerUser(name:string,email:string,password:string,address:string):User
        +charge(userId:long,amount:bigdecimal):User
        +getCoupon(userId:long):UserCoupon
        +getPoint(userId:long):User
        +usePoint(userId:long,amount:bigdecimal):User
    }

    class PRODUCE_SERVICE {
        +getProduct(productId:long) : Product
        +getAll(filter:ProductFilter,includeOutOfStock:boolean): List<Product>
        +searchByName(name:string,includeOutOfStock:boolean) : List<Product>
        +updateStock(productId: long, quantity :int) : Product
        +updateInfo(name:string,category:string,description:string,price:bigdecimal,status:enum): Product
        +subInventory(productId:long,quantity:int)
    }

    class ProductFilter {
        float minPrice
        float maxPrice
        string category
        Sort sort
    }

    class PAYMENT_SERVICE {
        +processPayment(orderId: long, paymentMethod: enum)
        +refundPayment(paymentId: long)
        +getHistory(userId:long)
    }

    class ORDER_SERVICE {
        +create(userId:long,products:List<Product>,couponIds:List<UUID>)
        +getHistory(userId:long)
        +update(orderId:long,price:bigdecimal)
        +cancel(orderId:long)			 
    }

    class COUPON_SERVICE {
        +createCoupon(discount: float, stock: int, type: enum, expiredAt: timestamp)
        +issueCoupon(userId: long)
        +delete(couponId:long)
        +update(couponId:long,status:enum,name:string,description:string)
    }

    class ProductRepository {
        +updateStock(id:long,stock:int)
        +findById(id: long): Product
        +findByFilters(filter: ProductFilter, includeOutOfStock: boolean): List<Product>
        +findStockById(id: long): int
        +findByName(name: string, includeOutOfStock: boolean): List<Product>
    }

    class PointHistoryRepository {
        +save(history:PointHistory)
        +update(pointId:long,amount:bigdecimal)
    }

    class OrderHistoryRepository {
        +save(history:OrderHistory)
        +update(orderId:long,status:enum)
    }

    class OrderRepository {
        +save(order: Order)
        +updatePrice(total:bigdecimal,discount:bigdecimal)
        +findById(id: long): Order
        +findByUserId(userId: long): Order
        +findStockById(id: long): int
    }

    class CouponRepository {
        +findAvailableByUserId(userId:long)
        +findByUserId(userId:long)
        +save(coupon:Coupon)
    }

    class PaymentRepository {
        +save(payment: Payment)
        +update(status:enum)
        +findByUserId(userId: long): List<Payment>
        +findById(id: long): Payment
    }

    class EventStore {
        long id
        string eventType
        string eventData
        timestamp occurredAt
    
        +storeEvent(event: string, data: string)
        +replayEvents()
    }

    ProductService --> ProductRepository : uses
    OrderService --> OrderRepository : uses
    PaymentService --> PaymentRepository : uses
    CouponService --> UserCoupon : manages

    EventStore <-- Order : records
    EventStore <-- Payment : records
    EventStore <-- Product : records
