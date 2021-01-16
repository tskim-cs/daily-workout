package workout.dailyworkout.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guide {

    @Id @GeneratedValue
    @Column(name = "guide_id")
    private Long id;

    private String trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public static Guide createGuide(String trainer, Workout workout, String content) {
        Guide guide = new Guide();
        guide.trainer = trainer;
        guide.workout = workout;
        guide.content = content;

        return guide;
    }

    public void updateGuideContent(String content) {
        this.content = content;
    }
}
