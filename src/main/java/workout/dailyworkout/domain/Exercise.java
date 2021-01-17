package workout.dailyworkout.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exercise {

    @Id @GeneratedValue
    @Column(name = "exercise_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseEquip equip;

    public static Exercise createExercise(String name, ExerciseType type, ExerciseEquip equip) {
        Exercise exercise = new Exercise();
        exercise.name = name;
        exercise.type = type;
        exercise.equip = equip;

        return exercise;
    }

    public void updateExerciseName(String name) {
        this.name = name;
    }
}
