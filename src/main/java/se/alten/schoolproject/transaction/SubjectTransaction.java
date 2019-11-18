package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Subject;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;


@Stateless
@Default
public class SubjectTransaction implements SubjectTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllSubjects() {
        Query query = entityManager.createQuery("SELECT DISTINCT s FROM Subject s JOIN FETCH s.students b");
        return query.getResultList();
    }

    @Override
    public Subject addSubject(Subject subject) {
        try {
            entityManager.persist(subject);
            entityManager.flush();
            return subject;
        } catch ( PersistenceException pe ) {
            subject.setTitle("duplicate");
            return subject;
        }
    }

    @Override
    public List<Subject> getSubjectByName(List<String> subject) {

        String queryStr = "SELECT sub FROM Subject sub WHERE sub.title IN :subject";
        TypedQuery<Subject> query = entityManager.createQuery(queryStr, Subject.class);
        query.setParameter("subject", subject);

        return query.getResultList();
     }

    @Override
    public void removeSubject(Long id) {
        Query query = entityManager.createQuery("DELETE FROM Subject s WHERE s.id = :id");
        query.setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void updateSubject(Long id, String newSubject) {
        Query updateQuery = entityManager.createNativeQuery("UPDATE subject SET title = :newSubject WHERE id = :id");
        updateQuery.setParameter("title", newSubject)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Subject findById(Long id) {
        Subject subject = (Subject) entityManager.createQuery("SELECT s FROM Subject s WHERE s.id = :id")
                .setParameter("id", id).getSingleResult();
        return subject;
    }

    @Override
    public void addStudentToSubject(Subject subjectToUpdate) {
        Query updateQuery = entityManager.createNativeQuery("UPDATE subject SET stu = :newSubject WHERE id = :id");
    }
}
