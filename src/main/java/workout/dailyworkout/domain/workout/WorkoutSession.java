package workout.dailyworkout.domain.workout;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.dailyworkout.domain.Exercise;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutSession {

    @Id @GeneratedValue
    @Column(name = "workout_session_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private Duration duration = Duration.ZERO;

    // [Read-only] Change to this will not be applied to DB.
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<WorkoutSet> sets = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<WorkoutExercise> relatedExercises = new ArrayList<>();

    public static WorkoutSession createWorkoutSession(LocalDateTime date) {
        WorkoutSession session = new WorkoutSession();
        session.startDate = date;

        return session;
    }

    public static WorkoutSession createWorkoutSessionNow() {
        return createWorkoutSession(LocalDateTime.now());
    }

    public void addWorkoutSet(WorkoutSet set) {
        this.sets.add(set);
        set.connectSession(this);

        // Add related exercises
        if (!this.relatedExercisesContain(set.getExercise())) {
            WorkoutExercise workoutExercise = WorkoutExercise.createWorkoutExercise(set.getExercise(), this);
            this.relatedExercises.add(workoutExercise);

        }

        // Increase duration time from start to last(this) set
        this.recordDuration();
    }

    public WorkoutSet findWorkoutSet(Long setId) {
        for (WorkoutSet set : this.sets) {
            if (set.getId().equals(setId))
                return set;
        }
        return null;
    }

    public void removeWorkoutSet(WorkoutSet set) {
        this.sets.remove(set);
        set.removeRelation();

        // If there is no related exercise which was in removed set, remove the exercise from relatedExercises.
        if (!setsContain(set.getExercise())) {
            this.relatedExercises.removeIf(e -> e.exerciseIs(set.getExercise()));
        }

        // Re-record duration because it can remove last set
        this.recordDuration();
    }

    public boolean sessionContains(Exercise exercise) {
        return relatedExercisesContain(exercise);
    }

    private void recordDuration() {
        // TODO
        // Only single set exists, how can record duration?

        if (sets.isEmpty())
            return;

        duration = Duration.between(startDate, sets.get(sets.size() - 1).getCreatedDate());
    }

    private boolean setsContain(Exercise exercise) {
        return sets.stream().anyMatch(s -> s.getExercise().equals(exercise));
    }

    private boolean relatedExercisesContain(Exercise exercise) {
        return relatedExercises.stream().anyMatch(e -> e.exerciseIs(exercise));
    }
}
