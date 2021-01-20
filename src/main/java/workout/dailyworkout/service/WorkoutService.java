package workout.dailyworkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.workout.WorkoutSet;
import workout.dailyworkout.repository.ExerciseRepository;
import workout.dailyworkout.repository.WorkoutRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Long startSession() {
        WorkoutSession session = WorkoutSession.createWorkoutSessionNow();
        workoutRepository.save(session);
        return session.getId();
    }

//    @Transactional
//    public Long createPastSession(LocalDateTime date) {
//        if (date.isAfter(LocalDateTime.now())) {
//            throw new IllegalArgumentException("The time should be past.");
//        }
//        WorkoutSession session = WorkoutSession.createWorkoutSession(date);
//        workoutRepository.save(session);
//        return session.getId();
//    }

    @Transactional
    public Long endSession(Long sessionId) {
        WorkoutSession session = workoutRepository.findSessionById(sessionId);

        // TODO
        // Make immutable WorkoutSet list
        // TODO
        // How to recognize non-ended session and force user to end it?

        workoutRepository.save(session);
        return session.getId();
    }

    @Transactional
    public Long addSet(Long sessionId, Long exerciseId, int weight, int reps) {
        Exercise exercise = exerciseRepository.findById(exerciseId);
        WorkoutSession session = workoutRepository.findSessionById(sessionId);
        WorkoutSet set = WorkoutSet.addWorkoutSetNow(session, exercise, weight, reps);
        workoutRepository.save(session);
        return set.getId();
    }

    @Transactional
    public void removeSet(Long setId) {
        WorkoutSet set = workoutRepository.findSetById(setId);
        WorkoutSession session = set.getSession();
        set.removeWorkoutSet();
        workoutRepository.save(session);
        workoutRepository.removeSet(set);
    }

    @Transactional
    public void removeSession(Long sessionId) {
        workoutRepository.removeSession(workoutRepository.findSessionById(sessionId));
    }

    public WorkoutSession findSession(Long sessionId) {
        return workoutRepository.findSessionById(sessionId);
    }

    public WorkoutSet findSet(Long setId) {
        return workoutRepository.findSetById(setId);
    }
//
//    // TODO
//    // public List<Workout> findRecordInDay(LocalDateTime date) {}
//    // public List<Workout> findRecordInWeek(LocalDateTime date) {}
//    // public List<Workout> findRecordInBetween(LocalDateTime date) {}
}
