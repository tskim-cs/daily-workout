package workout.dailyworkout.repository;

import org.hibernate.jdbc.Work;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.workout.WorkoutSet;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutRepository {

    void save(WorkoutSession session);

    WorkoutSession findSessionById(Long sessionId);

    List<WorkoutSet> findCommonExerciseSets(Exercise exercise);
    List<WorkoutSession> findByDate(LocalDateTime date);
    List<WorkoutSession> findAllSessions();
    WorkoutSession findLastSession();

    void removeSession(WorkoutSession session);
}
