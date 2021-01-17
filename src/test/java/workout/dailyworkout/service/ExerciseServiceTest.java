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
import workout.dailyworkout.repository.ExerciseRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ExerciseServiceTest {

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Test
    public void addExercise() throws Exception {
        // Given
        Exercise exercise = Exercise.createExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);

        // When
        Long id = exerciseService.addExercise(exercise);

        // Then
        assertEquals(exercise, exerciseService.findOne(id));
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedWorkout() throws Exception {
        // Given
        Exercise exercise1 = Exercise.createExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long id = exerciseService.addExercise(exercise1);

        // When
        Exercise exercise2 = Exercise.createExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        
        // Then
        Long id2 = exerciseService.addExercise(exercise2);

        fail();
    }

    @Test
    public void updateWorkoutTest() throws Exception {
        // Given
        Exercise exercise = Exercise.createExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        Long id = exerciseService.addExercise(exercise);

        // When
        exerciseService.updateExerciseName(id, "barbell squat");

        // Then
        assertEquals(exercise.getName(), "barbell squat");
    }


}