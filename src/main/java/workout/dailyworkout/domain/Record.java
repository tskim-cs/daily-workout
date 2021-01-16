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

    private LocalDateTime recordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private int weight;

    private int reps;
}
