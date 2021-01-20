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

    public static WorkoutSet addWorkoutSet(WorkoutSession session, Exercise exercise, int weight, int reps, LocalDateTime date) {
        WorkoutSet workoutSet = new WorkoutSet();
        workoutSet.createdDate = date;
        workoutSet.session = session;
        workoutSet.exercise = exercise;
        workoutSet.weight = weight;
        workoutSet.reps = reps;

        // Add this set to WorkoutSession.sets
        session.getSets().add(workoutSet);
        // Increase duration time from start to last(this) set
        session.recordDuration();

        return workoutSet;
    }

    public static WorkoutSet addWorkoutSetNow(WorkoutSession session, Exercise exercise, int weight, int reps) {
        return addWorkoutSet(session, exercise, weight, reps, LocalDateTime.now());
    }

    public void removeWorkoutSet() {
        WorkoutSession session = this.getSession();
        session.getSets().remove(this);

        // Re-record duration because it can remove last set
        session.recordDuration();

        // As removing pointer to this WorkoutSet, they will be removed.
        this.session = null;
        this.exercise = null;
    }
}
