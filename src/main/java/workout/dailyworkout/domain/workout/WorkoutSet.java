package workout.dailyworkout.domain.workout;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.dailyworkout.domain.Exercise;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutSet {

    @Id @GeneratedValue
    @Column(name = "workout_set_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id")
    private WorkoutSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int weight;
    private int reps;

    public static WorkoutSet createWorkoutSet(Exercise exercise, int weight, int reps, LocalDateTime date) {
        WorkoutSet workoutSet = new WorkoutSet();
        workoutSet.createdDate = date;
        workoutSet.exercise = exercise;
        workoutSet.weight = weight;
        workoutSet.reps = reps;

        return workoutSet;
    }

    public static WorkoutSet createWorkoutSetNow(Exercise exercise, int weight, int reps) {
        return createWorkoutSet(exercise, weight, reps, LocalDateTime.now());
    }

    public void connectSession(WorkoutSession session) {
        // Disallow changing existing session
        if (this.session != null)
            throw new IllegalStateException();
        else
            this.session = session;
    }

    public void removeRelation() {
        this.session = null;
        this.exercise = null;
    }
}
