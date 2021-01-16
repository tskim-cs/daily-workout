package workout.dailyworkout.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workout {

    @Id @GeneratedValue
    @Column(name = "workout_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private WorkoutPart workoutPart;

    @Column(nullable = false)
    private WorkoutEquip workoutEquip;

    public void createWorkout(String name, WorkoutPart part, WorkoutEquip equip) {
        this.name = name;
        this.workoutPart = part;
        this.workoutEquip = equip;
    }

    public void updateWorkoutName(String name) {
        this.name = name;
    }
}
