package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

@Stateless
@Default
public class TeacherTransaction implements TeacherTransactionAccess {

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List<Teacher> listAllTeachers() {
        Query query = entityManager.createQuery("SELECT t from Teacher t");
        return (List<Teacher>) query.getResultList();
    }

    @Override
    public Teacher addTeacher(Teacher teacher) {
        try {
            entityManager.persist(teacher);
            entityManager.flush();
            return teacher;
        } catch ( PersistenceException pe ) {
            teacher.setTeacherFirstName("duplicate");
            return teacher;
        }
    }

    @Override
    public void removeTeacher(String email) {
        Query query = entityManager.createQuery("DELETE FROM Teacher t WHERE t.teacherEmail = :email");
        query.setParameter("email", email)
                .executeUpdate();
    }

    @Override
    public void updateTeacher(String forename, String lastName, String email) {

    }
}
