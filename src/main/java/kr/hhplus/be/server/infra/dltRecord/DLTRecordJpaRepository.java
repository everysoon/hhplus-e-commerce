package kr.hhplus.be.server.infra.dltRecord;

import kr.hhplus.be.server.domain.dltRecord.DLTRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DLTRecordJpaRepository extends JpaRepository<DLTRecord, Long> {
	List<DLTRecord> findAllByResolvedNot();
	List<DLTRecord> findAllByDlqEventType();
	List<DLTRecord> findAllByUserId();
}
