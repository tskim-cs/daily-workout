package workout.dailyworkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Record;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.repository.RecordRepository;
import workout.dailyworkout.repository.ExerciseRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final RecordRepository recordRepository;

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

    // TODO
    // public List<Workout> findWorkouts(WorkoutSearch workoutSearch) {}

    public List<Exercise> findAllWorkouts() {
        return exerciseRepository.findAll();
    }

    @Transactional
    public void updateExerciseName(Long id, String name) {
        Exercise exercise = exerciseRepository.findById(id);
        exercise.updateExerciseName(name);
    }

    public Record getLastRecord(Long id) {
        Exercise exercise = exerciseRepository.findById(id);
        List<Record> records = recordRepository.findByExercise(exercise);
        return records.size() > 0 ? records.get(records.size() - 1) : null;
    }

    // TODO
    // public Record getRecentDayRecord(Long id) {}
}
