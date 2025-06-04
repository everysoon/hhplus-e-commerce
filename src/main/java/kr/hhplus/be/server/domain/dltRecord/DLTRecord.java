package kr.hhplus.be.server.domain.dltRecord;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.utils.JsonUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Table(name = "dlt_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DLTRecord {
	public enum DLTEventType {
		COUPON_ISSUE,
		ORDER_PAID,
		CANCEL_ORDER_PAID,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private DLTEventType eventType;
	private String messageKey;
	private String topicName;
	private String value;
	private int partitionNum;
	@Column(name = "message_offset")
	private long offset;
	@Column(updatable = false)
	private LocalDateTime failedAt;
	private boolean resolved; // 재처리 완료되었는지

	@Builder
	public DLTRecord(DLTEventType eventType, String messageKey,String topicName,String value,int partitionNum,long offset,Long timestamp,boolean resolved) {
		this.eventType = eventType;
		this.messageKey = messageKey;
		this.topicName = topicName;
		this.value = value;
		this.partitionNum = partitionNum;
		this.offset = offset;
		this.failedAt = Instant.ofEpochMilli(timestamp)
			.atZone(ZoneId.systemDefault())  // 시스템 기본 시간대
			.toLocalDateTime();
		this.resolved = resolved;
	}

	public void markResolved() {
		this.resolved = true;
	}

	public void setValue(Object event){
		this.value = JsonUtil.toJson(event);
	}
	public <T> T getValueObj(Class<T> clazz) {
		return JsonUtil.fromJson(this.value, clazz);
	}
}
