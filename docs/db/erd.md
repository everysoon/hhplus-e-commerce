erDiagram
    USER {
        LONG id PK
        STRING name 
        STRING email
        STRING password
        STRING address
        DECIMAL point
        TIMESTAMP created_at
    }
		%% user-coupon-status : issued, used, expired,revoked, pending
    USER_COUPON {
        LONG id PK
        LONG user_id FK
        LONG coupon_id FK
        STRING status 
        INT remaining_stock
        TIMESTAMP issued_at
    }
    %% point-status : charged, used
    POINT_HISTORY {
        LONG id PK
        LONG user_id FK
        STRING status
        DECIMAL amount
        TIMESTAMP created_at
    }

    "ORDER" {
        LONG id PK
        LONG user_id FK
        LONG payment_id FK
        DECIMAL total_price
        DECIMAL total_discount
        TIMESTAMP created_at
    }

    ORDER_ITEM {
        LONG id PK
        LONG order_id FK
        LONG product_id FK
        INT quantity
    }
     %% product-status: available, out_of_stock, reserved
    PRODUCT {
        LONG id PK
        STRING category
        STRING status
        STRING product_name
        STRING description
        FLOAT price
        INT stock
        TIMESTAMP created_at
    }

    COUPON {
        VARCHAR(50) id PK
        FLOAT discount
        STRING description
        INT stock
        ENUM type
        TIMESTAMP expired_at
        TIMESTAMP created_at
    }

    PAYMENT {
        LONG id PK
        LONG order_id FK
        ENUM payment_method
        TIMESTAMP created_at
    }
%% payment-status: card,bank_transfer,points,paypal
    PAYMENT_HISTORY {
        LONG id PK
        LONG payment_id FK
        STRING payment_method 
        STRING description
        TIMESTAMP created_at
        DECIMAL amount
    }
%% order-status: pending,shipped,delivered,canceled
    ORDER_HISTORY {
        LONG id PK
        LONG order_id FK
        STRING status 
        TIMESTAMP created_at
    }

    USER ||--o{ "ORDER" : places
    USER ||--o{ USER_COUPON : has
    USER_COUPON }o--|| COUPON : is_assigned_to
    USER ||--o{ POINT_HISTORY : has
    "ORDER" ||--o{ ORDER_ITEM : contains
    "ORDER" ||--|| PAYMENT : has
    "ORDER" ||--o{ ORDER_HISTORY : has
    PAYMENT ||--o{ PAYMENT_HISTORY : has
    COUPON ||--o{ "ORDER" : used_in
    PRODUCT ||--o{ ORDER_ITEM : used_in
