package workout.dailyworkout.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.workout.WorkoutSet;
import workout.dailyworkout.repository.WorkoutRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkoutSessionServiceTest {

    @Autowired
    WorkoutService workoutService;

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    ExerciseService exerciseService;

    public Long addExercise(String name, ExerciseType type, ExerciseEquip equip) throws Exception {
        Exercise exercise = Exercise.createExercise(name, type, equip);
        return exerciseService.addExercise(exercise);
    }

    @Test
    public void startWorkoutSessionWithoutSet() throws Exception {
        // Given
        addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);

        // When
        Long id = workoutService.startSession();

        // Then
        assert(workoutRepository.findSessionById(id) != null);
    }

    @Test
    public void addSetsNoEndSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        // When
        int len = 3;
        List<Long> setIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            setIds.add(workoutService.addSet(sessionId, exerciseId, 50, 15));
        }

        // Then
        // Check session identical
        assertEquals(1, workoutRepository.findAllSessions().size());
        assertEquals(sessionId, workoutRepository.findAllSessions().get(0).getId());
        assertEquals(workoutRepository.findSessionById(sessionId), workoutRepository.findAllSessions().get(0));

        // Check session contains all sets
        for (Long id : setIds) {
            assert(workoutService.findSession(sessionId).getSets().contains(workoutService.findSet(id)));
            assertEquals(workoutService.findSession(sessionId), workoutService.findSet(id).getSession());
        }

        // Check saved on exercise side
        for (Long id : setIds) {
            assert(exerciseService.findLastWorkoutSets(exerciseId).contains(workoutService.findSet(id)));
        }
    }

    @Test
    public void addCompleteSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        int len = 3;
        List<Long> setIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            setIds.add(workoutService.addSet(sessionId, exerciseId, 50, 15));
        }

        // When
        workoutService.endSession(sessionId);

        // Then
        // Check session identical
        assertEquals(1, workoutRepository.findAllSessions().size());
        assertEquals(sessionId, workoutRepository.findAllSessions().get(0).getId());
        assertEquals(workoutRepository.findSessionById(sessionId), workoutRepository.findAllSessions().get(0));

        // Check session contains all sets
        for (Long id : setIds) {
            assert(workoutService.findSession(sessionId).getSets().contains(workoutService.findSet(id)));
            assertEquals(workoutService.findSession(sessionId), workoutService.findSet(id).getSession());
        }

        // Check saved on exercise side
        for (Long id : setIds) {
            assert(exerciseService.findLastWorkoutSets(exerciseId).contains(workoutService.findSet(id)));
        }
    }

    @Test
    public void getEmptySessionDuration() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);

        // When
        Long sessionId = workoutService.startSession();

        // Then
        assertEquals(Duration.ZERO, workoutService.findSession(sessionId).getDuration());
    }


    @Test
    public void setDurationTestDuringSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        for (int i = 0; i < 3; i++) {
            workoutService.addSet(sessionId, exerciseId, 50, 15);
        }

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);
        List<WorkoutSet> sets = session.getSets();
        assertEquals(Duration.between(session.getStartDate(), sets.get(sets.size() - 1).getCreatedDate()), session.getDuration());
    }

    @Test
    public void setDurationTestAfterSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        for (int i = 0; i < 3; i++) {
            workoutService.addSet(sessionId, exerciseId, 50, 15);
        }

        // When
        workoutService.endSession(sessionId);

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);
        List<WorkoutSet> sets = session.getSets();
        assertEquals(Duration.between(session.getStartDate(), sets.get(sets.size() - 1).getCreatedDate()), session.getDuration());
    }

    @Test
    public void setDurationTestAfterRemovingLastSet() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        for (int i = 0; i < 3; i++) {
            workoutService.addSet(sessionId, exerciseId, 50, 15);
        }
        Long lastSetId = workoutService.addSet(sessionId, exerciseId, 50, 15);
        workoutService.endSession(sessionId);

        // When
        workoutService.removeSet(lastSetId);

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);
        List<WorkoutSet> sets = session.getSets();
        assertEquals(Duration.between(session.getStartDate(), sets.get(sets.size() - 1).getCreatedDate()), session.getDuration());
    }


    @Test
    public void removeSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        int len = 3;
        List<Long> setIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            setIds.add(workoutService.addSet(sessionId, exerciseId, 50, 15));
        }
        workoutService.endSession(sessionId);

        // When
        workoutService.removeSession(sessionId);

        // Then
        // Check session empty
        assertEquals(0, workoutRepository.findAllSessions().size());

        // Check whether sets in session are cleared
        for (Long id : setIds) {
            assert(workoutRepository.findAllSets().isEmpty());
        }

        // Check saved on exercise side
        assert(exerciseService.findLastWorkoutSets(exerciseId).isEmpty());
    }
    
    @Test
    public void removeSet() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        int len = 3;
        List<Long> setIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            setIds.add(workoutService.addSet(sessionId, exerciseId, 50, 15));
        }
        workoutService.endSession(sessionId);

        // When
        Long removedSetId = setIds.get(0);
        workoutService.removeSet(removedSetId);
        setIds.remove(0);

        // Then
        // Check session identical
        assertEquals(1, workoutRepository.findAllSessions().size());
        assertEquals(sessionId, workoutRepository.findAllSessions().get(0).getId());
        assertEquals(workoutRepository.findSessionById(sessionId), workoutRepository.findAllSessions().get(0));

        // Check session contains all sets
        for (Long id : setIds) {
            assert(workoutService.findSession(sessionId).getSets().contains(workoutService.findSet(id)));
            assertEquals(workoutService.findSession(sessionId), workoutService.findSet(id).getSession());
        }
        assertEquals(2, workoutService.findSession(sessionId).getSets().size());
        assertEquals(2, workoutRepository.findSetsInSession(workoutRepository.findSessionById(sessionId)).size());

        // Check saved on exercise side
        for (Long id : setIds) {
            assert(exerciseService.findLastWorkoutSets(exerciseId).contains(workoutService.findSet(id)));
        }
        assertEquals(2, exerciseService.findLastWorkoutSets(exerciseId).size());

    }
}