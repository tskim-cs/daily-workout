package workout.dailyworkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.workout.WorkoutSet;
import workout.dailyworkout.repository.WorkoutRepository;
import workout.dailyworkout.repository.ExerciseRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;


    private void validateDuplicateExercise(Exercise exercise) {
        if (!exerciseRepository.findByName(exercise.getName()).isEmpty()) {
            throw new IllegalStateException("Workout already exists.");
        }
    }

    @Transactional
    public Long addExercise(Exercise exercise) {
        validateDuplicateExercise(exercise);
        exerciseRepository.save(exercise);
        return exercise.getId();
    }

    @Transactional
    public void updateExerciseName(Long id, String name) {
        Exercise exercise = exerciseRepository.findById(id);
        exercise.updateExerciseName(name);
    }

    public Exercise findOne(Long exerciseId) {
        return exerciseRepository.findById(exerciseId);
    }

    public List<Exercise> findByName(String name) { return exerciseRepository.findByName(name); }


    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

    /**
     *
     * @param exerciseId
     * @return List of WorkoutSet
     */
    public List<WorkoutSet> findWorkoutSetsInLastSession(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId);
        WorkoutSession lastSession = workoutRepository.findLastSessionWithExercise(exercise);

        if (lastSession == null) {
            return Collections.emptyList();
        }

        return findCommonExerciseSetsInSession(lastSession, exercise);
    }

    private List<WorkoutSet> findCommonExerciseSetsInSession(WorkoutSession session, Exercise exercise) {
        List<WorkoutSet> result = new ArrayList<>();
        for (WorkoutSet s : session.getSets()) {
            if (s.getExercise() == exercise) {
                result.add(s);
            }
        }
        return result;
    }

    // TODO
    // public List<Exercise> findExercises(ExerciseSearch exerciseSearch) {}
    // TODO
    // public Record getRecentDayRecord(Long id) {}
}
