package kr.hhplus.be.server.infra.dlqRecord;

import kr.hhplus.be.server.domain.dlqRecord.DLQRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DLQRecordJpaRepository extends JpaRepository<DLQRecord, Long> {
	List<DLQRecord> findAllByResolvedNot();
	List<DLQRecord> findAllByDlqEventType();
	List<DLQRecord> findAllByUserId();
}
