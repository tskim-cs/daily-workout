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
        for (ExerciseEquip e : ExerciseEquip.values()) {
            names.add(e.toString());
        }
        return names;
    }
}
