package workout.dailyworkout.service;

import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Record;
import workout.dailyworkout.domain.Workout;
import workout.dailyworkout.domain.WorkoutEquip;
import workout.dailyworkout.domain.WorkoutPart;
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
    WorkoutService workoutService;

    public Workout addWorkout() throws Exception {
        Workout workout = Workout.createWorkout("squat", WorkoutPart.LEGS, WorkoutEquip.BARBELL);
        Long id = workoutService.addWorkout(workout);
        return workout;
    }

    @Test
    public void addRecord() throws Exception {
        // Given
        Workout workout = addWorkout();
        Record record = Record.createRecord(workout, 50, 15, LocalDateTime.now());

        // When
        Long id = recordService.addRecord(record);

        // Then
        assertEquals(record, recordService.findOne(id));
        assertEquals(workout, record.getWorkout());
        assertEquals(record, workoutService.getLastRecord(workout.getId()));
    }

    @Test
    public void addRecords() throws Exception {
        // Given
        Workout workout = addWorkout();

        // When
        Long id = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));
        Long id2 = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));
        Long id3 = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));

        // Then
        List<Record> records = recordRepository.findAll();
        assertEquals(3, records.size());
    }
    
    @Test
    public void getLastRecord() throws Exception {
        // Given
        Workout workout = addWorkout();

        // When
        Long id = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));
        Long id2 = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));
        Long id3 = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));

        // Then
        assertEquals(id3, workoutService.getLastRecord(workout.getId()).getId());
    }

    @Test
    public void removeRecord() throws Exception {
        // Given
        Workout workout = addWorkout();

        Long id = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));
        Long id2 = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));
        Long id3 = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));

        // When
        recordService.removeOne(id3);

        // Then
        assertEquals(2, recordRepository.findAll().size());
        assertEquals(id2, workoutService.getLastRecord(workout.getId()).getId());
    }
    
    @Test
    public void findSameDateRecord() throws Exception {
        // Given
        Workout workout = addWorkout();

        // When
        Long id = recordService.addRecord(Record.createRecord(workout, 50, 15, LocalDateTime.now()));

        // Then
        assert(!recordRepository.findByDate(LocalDateTime.now()).isEmpty());
    }
        

}