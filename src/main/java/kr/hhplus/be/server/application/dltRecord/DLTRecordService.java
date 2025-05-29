package kr.hhplus.be.server.application.dltRecord;

import kr.hhplus.be.server.domain.dltRecord.DLTRecord;
import kr.hhplus.be.server.domain.dltRecord.DLTRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DLTRecordService {
	private final DLTRecordRepository dltRecordRepository;
	public void save(DLTRecord dlqRecord) {
		dltRecordRepository.save(dlqRecord);
	}
}
