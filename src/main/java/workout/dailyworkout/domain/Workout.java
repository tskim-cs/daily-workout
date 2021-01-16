package workout.dailyworkout.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.jdbc.Work;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private WorkoutPart part;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutEquip equip;

    public static Workout createWorkout(String name, WorkoutPart part, WorkoutEquip equip) {
        Workout workout = new Workout();
        workout.name = name;
        workout.part = part;
        workout.equip = equip;

        return workout;
    }

    public void updateWorkoutName(String name) {
        this.name = name;
    }
}
