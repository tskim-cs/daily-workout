package workout.dailyworkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Record;
import workout.dailyworkout.repository.RecordRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional
    public Long addRecord(Record record) {
        recordRepository.save(record);
        return record.getId();
    }

    public Record findOne(Long recordId) {
        return recordRepository.findById(recordId);
    }

    // TODO
    // public List<Workout> findRecordInDay(LocalDateTime date) {}
    // public List<Workout> findRecordInWeek(LocalDateTime date) {}
    // public List<Workout> findRecordInBetween(LocalDateTime date) {}

    @Transactional
    public void removeOne(Long id) {
        recordRepository.remove(id);
    }
}
