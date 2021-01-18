package workout.dailyworkout.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.repository.WorkoutRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Commit
public class WorkoutSessionServiceTest {

    @Autowired
    WorkoutService workoutService;

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ExerciseService exerciseService;
//
//    public Long addExercise(String name, ExerciseType type, ExerciseEquip equip) throws Exception {
//        Exercise exercise = Exercise.createExercise(name, type, equip);
//        return exerciseService.addExercise(exercise);
//    }
//
//    @Test
//    public void startWorkoutSessionWithoutSet() throws Exception {
//        // Given
//        addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
//
//        // When
//        Long id = workoutService.startSession();
//
//        // Then
//        assert(workoutRepository.findSessionById(id) != null);
//    }
//
//    @Test
//    public void addSetsNoEndSession() throws Exception {
//        // Given
//        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
//        Long sessionId = workoutService.startSession();
//
//        // When
//        int len = 3;
//        Long[] setIds = new Long[len];
//        for (Long id : setIds) {
//            id = workoutService.addSet(sessionId, exerciseId, 50, 15);
//        }
//
//        // Then
//
//        // Check session identical
//        assertEquals(1, workoutRepository.findAllSessions().size());
//        assertEquals(sessionId, workoutRepository.findAllSessions().get(0).getId());
//        assertEquals(workoutRepository.findSessionById(sessionId), workoutRepository.findAllSessions().get(0));
//
//        // Check session contains all sets
//        for (Long id : setIds) {
//            workoutService.findSession(sessionId).getSets().contains(workoutService.findSet(id));
//            assertEquals(workoutService.findSession(sessionId), workoutService.findSet(id).getSession());
//        }
//
//        // Check saved on exercise side
//        for (Long id : setIds) {
//            assert(exerciseService.findLastWorkoutSets(exerciseId).contains(workoutService.findSet(id)));
//        }
//    }
//
//    @Test
//    public void addCompleteSession() throws Exception {
//        // Given
//
//        // When
//
//        // Then
//    }
//
//
//    @Test
//    public void getLastRecord() throws Exception {
//        // Given
//        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
//
//        // When
////        Long id = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
////        Long id2 = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
////        Long id3 = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
//
//        // Then
//        assertEquals(id3, exerciseService.getLastRecord(exercise.getId()).getId());
//    }
//
//    @Test
//    public void removeRecord() throws Exception {
//        // Given
//        Exercise exercise = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
//
////        Long id = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
////        Long id2 = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
////        Long id3 = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
//
//        // When
//        workoutService.removeOne(id3);
//
//        // Then
//        assertEquals(2, workoutRepository.findAll().size());
//        assertEquals(id2, exerciseService.getLastRecord(exercise.getId()).getId());
//    }
//
//    @Test
//    public void findSameDateRecord() throws Exception {
//        // Given
//        Exercise exercise = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
//
//        // When
////        Long id = recordService.addRecord(WorkoutSession.createWorkout(exercise, 50, 15, LocalDateTime.now()));
//
//        // Then
//        assert(!workoutRepository.findByDate(LocalDateTime.now()).isEmpty());
//    }
//

}