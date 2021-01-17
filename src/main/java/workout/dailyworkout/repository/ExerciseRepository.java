package workout.dailyworkout.repository;

import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;

import java.util.List;

public interface ExerciseRepository {

    void save(Exercise exercise);
    Exercise findById(Long id);
    List<Exercise> findByName(String name);
    List<Exercise> findByType(ExerciseType type);
    List<Exercise> findByEquip(ExerciseEquip equip);
    List<Exercise> findAll();
}
