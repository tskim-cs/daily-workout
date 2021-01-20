package workout.dailyworkout.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.workout.WorkoutSet;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JPAWorkoutRepository implements WorkoutRepository {

    private final EntityManager em;

    @Override
    public void save(WorkoutSession session) {
        em.persist(session);
    }

    @Override
    public WorkoutSet findSetById(Long setId) {
        return em.find(WorkoutSet.class, setId);
    }

    @Override
    public WorkoutSession findSessionById(Long sessionId) {
        return em.find(WorkoutSession.class, sessionId);
    }

    @Override
    public List<WorkoutSet> findCommonExerciseSets(Exercise exercise) {
        return em.createQuery("select s from WorkoutSet s where s.exercise = :exercise", WorkoutSet.class)
                .setParameter("exercise", exercise)
                .getResultList();
    }

    @Override
    public List<WorkoutSession> findByDate(LocalDateTime date) {
        return em.createQuery("select w from WorkoutSession w where w.startDate = :startDate", WorkoutSession.class)
                .setParameter("startDate", date)
                .getResultList();
    }

    @Override
    public List<WorkoutSession> findAllSessions() {
        return em.createQuery("select w from WorkoutSession w", WorkoutSession.class)
                .getResultList();
    }

    @Override
    public List<WorkoutSet> findAllSets() {
        return em.createQuery("select s from WorkoutSet s", WorkoutSet.class)
                .getResultList();
    }

    @Override
    public WorkoutSession findLastSession() {
        return em.createQuery("select w from WorkoutSession w order by w.id desc", WorkoutSession.class)
                .getSingleResult();
    }

    @Override
    public void removeSession(WorkoutSession session) {
        em.remove(session);
    }

    @Override
    public void removeSet(WorkoutSet set) {
        em.remove(set);
    }

    @Override
    public List<WorkoutSet> findSetsInSession(WorkoutSession session) {
        return em.createQuery("select s from WorkoutSet s where s.session = :session", WorkoutSet.class)
                .setParameter("session", session)
                .getResultList();
    }
}
