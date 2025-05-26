package kr.hhplus.be.server.domain.dlqRecord;

import java.util.List;

public interface DLQRecordRepository{
	List<DLQRecord> findAllByResolvedNot();
	List<DLQRecord> findAllByEventType();
	List<DLQRecord> findAllByUserId();
	void save(DLQRecord dlqRecord);
}
