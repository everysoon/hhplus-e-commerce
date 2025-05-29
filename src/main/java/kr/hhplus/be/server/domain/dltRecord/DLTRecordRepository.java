package kr.hhplus.be.server.domain.dltRecord;

import java.util.List;

public interface DLTRecordRepository {
	List<DLTRecord> findAllByResolvedNot();
	List<DLTRecord> findAllByEventType();
	List<DLTRecord> findAllByUserId();
	void save(DLTRecord dlqRecord);
}
