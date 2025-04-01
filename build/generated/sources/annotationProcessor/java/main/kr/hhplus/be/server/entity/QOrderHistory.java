package kr.hhplus.be.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderHistory is a Querydsl query type for OrderHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderHistory extends EntityPathBase<OrderHistory> {

    private static final long serialVersionUID = -955457820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderHistory orderHistory = new QOrderHistory("orderHistory");

    public final NumberPath<Float> amount = createNumber("amount", Float.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOrder order;

    public QOrderHistory(String variable) {
        this(OrderHistory.class, forVariable(variable), INITS);
    }

    public QOrderHistory(Path<? extends OrderHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderHistory(PathMetadata metadata, PathInits inits) {
        this(OrderHistory.class, metadata, inits);
    }

    public QOrderHistory(Class<? extends OrderHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

