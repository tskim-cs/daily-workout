package workout.dailyworkout.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Workout;
import workout.dailyworkout.domain.WorkoutEquip;
import workout.dailyworkout.domain.WorkoutPart;
import workout.dailyworkout.repository.WorkoutRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkoutServiceTest {

    @Autowired
    WorkoutService workoutService;

    @Autowired
    WorkoutRepository workoutRepository;

    @Test
    public void addWorkout() throws Exception {
        // Given
        Workout workout = Workout.createWorkout("squat", WorkoutPart.LEGS, WorkoutEquip.BARBELL);

        // When
        Long id = workoutService.addWorkout(workout);

        // Then
        assertEquals(workout, workoutService.findOne(id));
    }

    @Test(expected = IllegalStateException.class)
    public void rejectDuplicatedWorkout() throws Exception {
        // Given
        Workout workout1 = Workout.createWorkout("squat", WorkoutPart.LEGS, WorkoutEquip.BARBELL);
        Long id = workoutService.addWorkout(workout1);

        // When
        Workout workout2 = Workout.createWorkout("squat", WorkoutPart.LEGS, WorkoutEquip.BARBELL);
        
        // Then
        Long id2 = workoutService.addWorkout(workout2);

        fail();
    }

    @Test
    public void updateWorkoutTest() throws Exception {
        // Given
        Workout workout = Workout.createWorkout("squat", WorkoutPart.LEGS, WorkoutEquip.BARBELL);
        Long id = workoutService.addWorkout(workout);

        // When
        workoutService.updateWorkoutName(id, "barbell squat");

        // Then
        assertEquals(workout.getName(), "barbell squat");
    }


}