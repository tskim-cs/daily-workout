package workout.dailyworkout.domain.workout;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    List<WorkoutSet> sets = new ArrayList<>();

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

        // Increase duration time from start to last(this) set
        this.recordDuration();
    }

    private void recordDuration() {
        // TODO
        // Only single set exists, how can record duration?

        if (sets.isEmpty())
            return;

        duration = Duration.between(startDate, sets.get(sets.size() - 1).getCreatedDate());
    }

    public WorkoutSet findWorkoutSet(Long setId) {
        for (WorkoutSet set : this.sets) {
            if (set.getId() == setId)
                return set;
        }
        return null;
    }

    public void removeWorkoutSet(WorkoutSet set) {
        this.sets.remove(set);
        set.removeRelation();

        // Re-record duration because it can remove last set
        this.recordDuration();
    }
}
