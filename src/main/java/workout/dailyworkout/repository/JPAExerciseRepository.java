package workout.dailyworkout.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JPAExerciseRepository implements ExerciseRepository {

    private final EntityManager em;

    @Override
    public void save(Exercise exercise) {
        em.persist(exercise);
    }

    @Override
    public Exercise findById(Long id) {
        return em.find(Exercise.class, id);
    }

    @Override
    public List<Exercise> findByName(String name) {
        return em.createQuery("select e from Exercise e where e.name = :name", Exercise.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Exercise> findByType(ExerciseType type) {
        return em.createQuery("select e from Exercise e where e.type = :type", Exercise.class)
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public List<Exercise> findByEquip(ExerciseEquip equip) {
        return em.createQuery("select e from Exercise e where e.type = :equip", Exercise.class)
                .setParameter("equip", equip)
                .getResultList();
    }

    @Override
    public List<Exercise> findAll() {
        return em.createQuery("select e from Exercise e", Exercise.class)
                .getResultList();
    }
}
