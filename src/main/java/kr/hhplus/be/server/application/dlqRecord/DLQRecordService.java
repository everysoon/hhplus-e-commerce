package kr.hhplus.be.server.application.dlqRecord;

import kr.hhplus.be.server.domain.dlqRecord.DLQRecord;
import kr.hhplus.be.server.domain.dlqRecord.DLQRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DLQRecordService {
	private final DLQRecordRepository dlqRecordRepository;
	public void save(DLQRecord dlqRecord) {
		dlqRecordRepository.save(dlqRecord);
	}
}
