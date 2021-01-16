package workout.dailyworkout.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import workout.dailyworkout.domain.Workout;
import workout.dailyworkout.domain.WorkoutEquip;
import workout.dailyworkout.domain.WorkoutPart;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JPAWorkoutRepository implements WorkoutRepository{

    private final EntityManager em;

    @Override
    public void save(Workout workout) {
        em.persist(workout);
    }

    @Override
    public Workout findById(Long id) {
        return em.find(Workout.class, id);
    }

    @Override
    public List<Workout> findByName(String name) {
        return em.createQuery("select w from Workout w where w.name = :name", Workout.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Workout> findByPart(WorkoutPart part) {
        return em.createQuery("select w from Workout w where w.part = :part", Workout.class)
                .setParameter("part", part)
                .getResultList();
    }

    @Override
    public List<Workout> findByEquip(WorkoutEquip equip) {
        return em.createQuery("select w from Workout w where w.part = :equip", Workout.class)
                .setParameter("equip", equip)
                .getResultList();
    }

    @Override
    public List<Workout> findAll() {
        return em.createQuery("select w from Workout w", Workout.class)
                .getResultList();
    }
}
