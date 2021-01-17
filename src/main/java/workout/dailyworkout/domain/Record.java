package workout.dailyworkout.domain;

import lombok.Getter;

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
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private int weight;
    private int reps;

    public static Record createRecord(Exercise exercise, int weight, int reps, LocalDateTime date) {
        Record record = new Record();
        record.exercise = exercise;
        record.recordDate = date;
        record.weight = weight;
        record.reps = reps;

        return record;
    }

    public static Record createRecordNow(Exercise exercise, int weight, int reps) {
        return createRecord(exercise, weight, reps, LocalDateTime.now());
    }
}
