package workout.dailyworkout.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import workout.dailyworkout.domain.Record;
import workout.dailyworkout.domain.Exercise;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JPARecordRepository implements RecordRepository{

    private final EntityManager em;

    @Override
    public void save(Record record) {
        em.persist(record);
    }

    @Override
    public Record findById(Long id) {
        return em.find(Record.class, id);
    }

    @Override
    public List<Record> findByExercise(Exercise exercise) {
        return em.createQuery("select r from Record r where r.exercise = :exercise", Record.class)
                .setParameter("exercise", exercise)
                .getResultList();
    }

    @Override
    public List<Record> findByDate(LocalDateTime date) {
        return em.createQuery("select r from Record r where r.recordDate = :recordDate", Record.class)
                .setParameter("recordDate", date)
                .getResultList();
    }

    @Override
    public List<Record> findAll() {
        return em.createQuery("select r from Record r", Record.class)
                .getResultList();
    }

    @Override
    public void remove(Long id) {
        em.remove(findById(id));
    }
}
