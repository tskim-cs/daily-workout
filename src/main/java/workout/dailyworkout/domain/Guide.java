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
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public static Guide createGuide(String trainer, Exercise exercise, String content) {
        Guide guide = new Guide();
        guide.trainer = trainer;
        guide.exercise = exercise;
        guide.content = content;

        return guide;
    }

    public void updateGuideContent(String content) {
        this.content = content;
    }
}
