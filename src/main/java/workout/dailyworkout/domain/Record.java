package workout.dailyworkout.domain;

import lombok.Getter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Record {

    @Id @GeneratedValue
    @Column(name = "record_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime recordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private int weight;
    private int reps;

    public static Record createRecord(Workout workout, int weight, int reps, LocalDateTime date) {
        Record record = new Record();
        record.workout = workout;
        record.recordDate = date;
        record.weight = weight;
        record.reps = reps;

        return record;
    }

    public static Record createRecordNow(Workout workout, int weight, int reps) {
        return createRecord(workout, weight, reps, LocalDateTime.now());
    }
}
