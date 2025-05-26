package kr.hhplus.be.server.infra.dlqRecord;

import kr.hhplus.be.server.domain.dlqRecord.DLQRecord;
import kr.hhplus.be.server.domain.dlqRecord.DLQRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DLQRecordJpaRepositoryImpl implements DLQRecordRepository {
	private final DLQRecordJpaRepository dlqRecordJpaRepository;

	@Override
	public List<DLQRecord> findAllByResolvedNot() {
		return dlqRecordJpaRepository.findAllByResolvedNot();
	}

	@Override
	public List<DLQRecord> findAllByEventType() {
		return dlqRecordJpaRepository.findAllByDlqEventType();
	}

	@Override
	public List<DLQRecord> findAllByUserId() {
		return dlqRecordJpaRepository.findAllByUserId();
	}

	@Override
	public void save(DLQRecord dlqRecord) {
		dlqRecordJpaRepository.save(dlqRecord);
	}
}
