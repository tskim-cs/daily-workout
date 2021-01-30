package workout.dailyworkout.domain.workout;

import org.junit.Test;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkoutSetTest {
    @Test
    public void removeWorkoutSetTest() throws Exception {
        // Given
        Exercise exercise = Exercise.createExercise("squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);
        WorkoutSession session = WorkoutSession.createWorkoutSessionNow();

        int len = 3;
        List<WorkoutSet> sets = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            WorkoutSet set = WorkoutSet.createWorkoutSetNow(exercise, 50, 15);
            session.addWorkoutSet(set);
            sets.add(set);
        }

        // When
        session.removeWorkoutSet(sets.get(0));

        // Then
        assertEquals(null, sets.get(0).getSession());
        assertEquals(false, session.getSets().contains(sets.get(0)));
        assertEquals(len - 1, session.getSets().size());
    }
}