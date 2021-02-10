package workout.dailyworkout.domain.workout;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.dailyworkout.domain.Exercise;

import javax.persistence.*;

/**
 * To save related exercises in given session
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutExercise {

    @Id
    @GeneratedValue
    @Column(name = "workout_exercise_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exericse_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private WorkoutSession session;

    public static WorkoutExercise createWorkoutExercise(Exercise exercise, WorkoutSession session) {
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.exercise = exercise;
        workoutExercise.session = session;
        return workoutExercise;
    }

    public boolean exerciseIs(Exercise exercise) {
        return this.exercise == exercise;
    }
}
