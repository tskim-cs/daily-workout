package workout.dailyworkout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.dailyworkout.domain.Workout;
import workout.dailyworkout.repository.WorkoutRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Transactional
    public Long addWorkout(Workout workout) {
        validateDuplicateWorkout(workout);
        workoutRepository.save(workout);
        return workout.getId();
    }

    private void validateDuplicateWorkout(Workout workout) {
        if (!workoutRepository.findByName(workout.getName()).isEmpty()) {
            throw new IllegalStateException("Workout already exists.");
        }
    }

    public Workout findOne(Long workoutId) {
        return workoutRepository.findById(workoutId);
    }

    // TODO
    // public List<Workout> findWorkouts(WorkoutSearch workoutSearch) {}

    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAll();
    }

    @Transactional
    public void updateWorkoutName(Long id, String name) {
        Workout workout = workoutRepository.findById(id);
        workout.updateWorkoutName(name);
    }
}
