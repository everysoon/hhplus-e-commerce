package kr.hhplus.be.server.domain.dlqRecord;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.utils.JsonUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "dlq_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DLQRecord {
	public enum DLQEventType {
		COUPON_ISSUE,
		ORDER_PAID,
		CANCEL_ORDER_PAID,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private DLQEventType dlqEventType;

	@Column(nullable = false, length = 200, columnDefinition = "TEXT")
	private String eventKey; // 실패한 이벤트의 고유 식별자 (예: 주문ID, 쿠폰ID 등 ..- JSON string으로 넣기)
	private int retryCount;
	private Long userId;
	private String errorMessage;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime failedAt;
	private boolean resolved; // 재처리 완료되었는지

	@Builder
	public DLQRecord(DLQEventType dlqEventType,Object eventKey,Long userId,String errorMessage) {
		this.errorMessage = errorMessage;
		this.dlqEventType = dlqEventType;
		this.userId = userId;
		setEventKey(eventKey);
		this.failedAt = LocalDateTime.now();
	}
	public void incrementRetryCount() {
		this.retryCount++;
	}

	public void markResolved() {
		this.resolved = true;
	}

	public void setEventKey(Object event){
		this.eventKey = JsonUtil.toJson(event);
	}
	public <T> T getEventKeyObj(Class<T> clazz) {
		return JsonUtil.fromJson(this.eventKey, clazz);
	}
}
