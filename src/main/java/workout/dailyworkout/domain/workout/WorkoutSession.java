package workout.dailyworkout.domain.workout;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private LocalDateTime workoutDate;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    List<WorkoutSet> sets = new ArrayList<WorkoutSet>();

    public static WorkoutSession createWorkoutSession(LocalDateTime date) {
        WorkoutSession session = new WorkoutSession();
        session.workoutDate = date;

        return session;
    }

    public static WorkoutSession createWorkoutSessionNow() {
        return createWorkoutSession(LocalDateTime.now());
    }
}
