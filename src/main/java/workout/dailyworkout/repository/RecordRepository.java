package workout.dailyworkout.repository;

import workout.dailyworkout.domain.Record;
import workout.dailyworkout.domain.Exercise;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository {

    void save(Record record);
    Record findById(Long id);
    List<Record> findByExercise(Exercise exercise);
    List<Record> findByDate(LocalDateTime date);
    List<Record> findAll();
    void remove(Long id);
}
