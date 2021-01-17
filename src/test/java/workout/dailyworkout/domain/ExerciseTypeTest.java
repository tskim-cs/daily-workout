package workout.dailyworkout.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ExerciseTypeTest {

    @Test
    public void enumToString() throws Exception {
        // Given
        ExerciseType type = ExerciseType.BACK;

        // Then
        assertEquals("Back", type.toString());
    }
    
    @Test
    public void stringToEnumValueOf() throws Exception {
        // Given
        String str1 = "Back";
        String str2 = "BACK";
        String str3 = "back";

        // Then
//        assertEquals(ExerciseType.BACK, ExerciseType.valueOf(str1));
        assertEquals(ExerciseType.BACK, ExerciseType.valueOf(str2));
//        assertEquals(ExerciseType.BACK, ExerciseType.valueOf(str3));
    }

    @Test
    public void stringToEnumFromString() throws Exception {
        // Given
        String str1 = "Back";
        String str2 = "BACK";
        String str3 = "back";

        // Then
        assertEquals(ExerciseType.BACK, ExerciseType.valueOf(str1.toUpperCase()));
        assertEquals(ExerciseType.BACK, ExerciseType.valueOf(str2.toUpperCase()));
        assertEquals(ExerciseType.BACK, ExerciseType.valueOf(str3.toUpperCase()));
    }

    @Test
    public void enumToStringList() throws Exception {
        // Given
        List<String> names = ExerciseType.names();

        System.out.println("names = " + names);
    }
    
}