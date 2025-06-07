package kr.hhplus.be.server.infra.dltRecord;

import kr.hhplus.be.server.domain.dltRecord.DLTRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DLTRecordJpaRepository extends JpaRepository<DLTRecord, Long> {
}
