package kr.hhplus.be.server.infra.dltRecord;

import kr.hhplus.be.server.domain.dltRecord.DLTRecord;
import kr.hhplus.be.server.domain.dltRecord.DLTRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DLTRecordJpaRepositoryImpl implements DLTRecordRepository {
	private final DLTRecordJpaRepository dltRecordJpaRepository;

	@Override
	public List<DLTRecord> findAllByResolvedNot() {
		return dltRecordJpaRepository.findAllByResolvedNot();
	}

	@Override
	public List<DLTRecord> findAllByEventType() {
		return dltRecordJpaRepository.findAllByDlqEventType();
	}

	@Override
	public List<DLTRecord> findAllByUserId() {
		return dltRecordJpaRepository.findAllByUserId();
	}

	@Override
	public void save(DLTRecord dlqRecord) {
		dltRecordJpaRepository.save(dlqRecord);
	}
}
