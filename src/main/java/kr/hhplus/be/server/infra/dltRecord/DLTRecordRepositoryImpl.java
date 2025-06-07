package kr.hhplus.be.server.infra.dltRecord;

import kr.hhplus.be.server.domain.dltRecord.DLTRecord;
import kr.hhplus.be.server.domain.dltRecord.DLTRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DLTRecordRepositoryImpl implements DLTRecordRepository {
	private final DLTRecordJpaRepository dltRecordJpaRepository;

	@Override
	public void save(DLTRecord dlqRecord) {
		dltRecordJpaRepository.save(dlqRecord);
	}
}
