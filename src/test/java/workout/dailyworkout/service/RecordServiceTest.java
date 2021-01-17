package workout.dailyworkout.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Record;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.repository.RecordRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Commit
public class RecordServiceTest {

    @Autowired
    RecordService recordService;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    ExerciseService exerciseService;

    public Exercise addWorkout() throws Exception {
        Exercise exercise = Exercise.createExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long id = exerciseService.addExercise(exercise);
        return exercise;
    }

    @Test
    public void addRecord() throws Exception {
        // Given
        Exercise exercise = addWorkout();
        Record record = Record.createRecord(exercise, 50, 15, LocalDateTime.now());

        // When
        Long id = recordService.addRecord(record);

        // Then
        assertEquals(record, recordService.findOne(id));
        assertEquals(exercise, record.getExercise());
        assertEquals(record, exerciseService.getLastRecord(exercise.getId()));
    }

    @Test
    public void addRecords() throws Exception {
        // Given
        Exercise exercise = addWorkout();

        // When
        Long id = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));
        Long id2 = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));
        Long id3 = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));

        // Then
        List<Record> records = recordRepository.findAll();
        assertEquals(3, records.size());
    }
    
    @Test
    public void getLastRecord() throws Exception {
        // Given
        Exercise exercise = addWorkout();

        // When
        Long id = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));
        Long id2 = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));
        Long id3 = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));

        // Then
        assertEquals(id3, exerciseService.getLastRecord(exercise.getId()).getId());
    }

    @Test
    public void removeRecord() throws Exception {
        // Given
        Exercise exercise = addWorkout();

        Long id = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));
        Long id2 = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));
        Long id3 = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));

        // When
        recordService.removeOne(id3);

        // Then
        assertEquals(2, recordRepository.findAll().size());
        assertEquals(id2, exerciseService.getLastRecord(exercise.getId()).getId());
    }
    
    @Test
    public void findSameDateRecord() throws Exception {
        // Given
        Exercise exercise = addWorkout();

        // When
        Long id = recordService.addRecord(Record.createRecord(exercise, 50, 15, LocalDateTime.now()));

        // Then
        assert(!recordRepository.findByDate(LocalDateTime.now()).isEmpty());
    }
        

}