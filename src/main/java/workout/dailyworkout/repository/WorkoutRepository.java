package workout.dailyworkout.repository;

import workout.dailyworkout.domain.Workout;
import workout.dailyworkout.domain.WorkoutEquip;
import workout.dailyworkout.domain.WorkoutPart;

import java.util.List;

public interface WorkoutRepository {

    void save(Workout workout);
    Workout findById(Long id);
    List<Workout> findByName(String name);
    List<Workout> findByPart(WorkoutPart part);
    List<Workout> findByEquip(WorkoutEquip equip);
    List<Workout> findAll();
}
