package workout.dailyworkout.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.workout.WorkoutSet;
import workout.dailyworkout.repository.WorkoutRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WorkoutServiceTest {

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
        for (int i = 0; i < 3; i++) {
            workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
        }

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);

        // Check session identical
        assertEquals(1, workoutRepository.findAllSessions().size());
        assertEquals(3, workoutService.findSetsInSession(sessionId).size());
    }

    @Test
    public void addCompleteSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        for (int i = 0; i < 3; i++) {
            workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
        }

        // When
        workoutService.endSession(sessionId);

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);

        // Check session identical
        assertEquals(1, workoutRepository.findAllSessions().size());
        assertEquals(3, workoutService.findSetsInSession(sessionId).size());
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
            workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
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
            workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
        }

        // When
        workoutService.endSession(sessionId);

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);
        List<WorkoutSet> sets = workoutService.findSetsInSession(sessionId);
        assertEquals(Duration.between(session.getStartDate(), sets.get(sets.size() - 1).getCreatedDate()), session.getDuration());
    }

    @Test
    public void setDurationTestAfterRemovingLastSet() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        for (int i = 0; i < 3; i++) {
            workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
        }
        WorkoutSet lastSet = workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
        workoutService.endSession(sessionId);

        // When
        workoutService.removeSet(sessionId, lastSet.getId());

        // Then
        WorkoutSession session = workoutService.findSession(sessionId);
        List<WorkoutSet> sets = workoutService.findSetsInSession(sessionId);
        assertEquals(Duration.between(session.getStartDate(), sets.get(sets.size() - 1).getCreatedDate()), session.getDuration());
    }

    @Test
    public void removeSession() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        int len = 3;
        List<Integer> setIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
        }
        workoutService.endSession(sessionId);

        // When
        workoutService.removeSession(sessionId);

        // Then
        // Check session empty
        assertEquals(0, workoutRepository.findAllSessions().size());

        // Check saved on exercise side
        assert(exerciseService.findWorkoutSetsInLastSession(exerciseId).isEmpty());
    }

    @Test
    public void removeSet() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        int len = 3;
        List<Long> setIds = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            WorkoutSet set = workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);
            setIds.add(set.getId());
        }
        workoutService.endSession(sessionId);

        // When
        Long removedSetId = setIds.get(0);
        workoutService.removeSet(sessionId, removedSetId);
        setIds.remove(0);

        // Then
        assertEquals(2, workoutService.findSetsInSession(sessionId).size());
        assertEquals(2, exerciseService.findWorkoutSetsInLastSession(exerciseId).size());
    }

    @Test
    public void getWorkoutSetIdTest() throws Exception {
        // Given
        Long exerciseId = addExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long sessionId = workoutService.startSession();

        // When
        WorkoutSet set = workoutService.addSetAndReturn(sessionId, exerciseId, 50, 15);

        // Then
        assertNotNull(set.getId());
    }
}