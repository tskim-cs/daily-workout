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

    @Transactional
    public Long addExercise(Exercise exercise) {
        validateDuplicateExercise(exercise);
        exerciseRepository.save(exercise);
        return exercise.getId();
    }

    private void validateDuplicateExercise(Exercise exercise) {
        if (!exerciseRepository.findByName(exercise.getName()).isEmpty()) {
            throw new IllegalStateException("Workout already exists.");
        }
    }

    public Exercise findOne(Long workoutId) {
        return exerciseRepository.findById(workoutId);
    }

    public List<Exercise> findByName(String name) { return exerciseRepository.findByName(name); }

    // TODO
    // public List<Exercise> findExercises(ExerciseSearch exerciseSearch) {}

    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public void updateExerciseName(Long id, String name) {
        Exercise exercise = exerciseRepository.findById(id);
        exercise.updateExerciseName(name);
    }

    public List<WorkoutSet> findLastWorkoutSets(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId);
        List<WorkoutSet> sets = workoutRepository.findCommonExerciseSets(exercise);

        if (sets.isEmpty()) {
            return Collections.emptyList();
        }

        WorkoutSession session = sets.get(sets.size() - 1).getSession();
        return findCommonExerciseSetsInSession(session, exercise);
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
    // public Record getRecentDayRecord(Long id) {}
}
