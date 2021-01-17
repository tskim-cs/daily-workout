package workout.dailyworkout.domain;

import java.util.ArrayList;
import java.util.List;

public enum ExerciseEquip {
    BARBELL, DUMBBELL, MACHINE, NONE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (ExerciseType t : ExerciseType.values()) {
            names.add(t.toString());
        }
        return names;
    }
}
