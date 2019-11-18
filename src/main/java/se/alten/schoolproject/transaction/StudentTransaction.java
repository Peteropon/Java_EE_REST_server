package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;

@Stateless
@Default
public class StudentTransaction implements StudentTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List<Student> listAllStudents() {
        Query query = entityManager.createQuery("SELECT DISTINCT s FROM Student s JOIN FETCH s.subject t", Student.class);
        return query.getResultList();
    }

    @Override
    public Student addStudent(Student studentToAdd) {
        try {
            entityManager.persist(studentToAdd);
            entityManager.flush();
            return studentToAdd;
        } catch ( PersistenceException pe ) {
            studentToAdd.setForename("duplicate");
            return studentToAdd;
        }
    }

    @Override
    public void removeStudent(String student) {
        Query query = entityManager.createQuery("DELETE FROM Student s WHERE s.email = :email");
        query.setParameter("email", student)
             .executeUpdate();
    }

    @Override
    public void updateStudent(String forename, String lastName, String email) {
        Query updateQuery = entityManager.createNativeQuery("UPDATE student SET forename = :forename, lastName = :lastName WHERE email = :email",
                Student.class);
        updateQuery.setParameter("forename", forename)
                   .setParameter("lastName", lastName)
                   .setParameter("email", email)
                   .executeUpdate();
    }

    @Override
    public void updateStudentPartial(Student student) {
        Student studentFound = (Student)entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                .setParameter("email", student.getEmail()).getSingleResult();

        Query query = entityManager.createQuery("UPDATE Student SET forename = :studentForename WHERE email = :email");
        query.setParameter("studentForename", student.getForename())
                .setParameter("email", studentFound.getEmail())
                .executeUpdate();
    }

    @Override
    public List findStudentByName(String forename) {
        Query studentsFound = entityManager.createQuery("SELECT s FROM Student s where s.forename = :forename");
        studentsFound.setParameter("forename", forename);

        return studentsFound.getResultList();
    }

    @Override
    public Student findStudentByEmail(String email) {
        TypedQuery<Student> studentWithThisEmail = entityManager.createQuery(
                "select s from Student s where s.email = :email", Student.class
        );
        studentWithThisEmail.setParameter("email", email);
        return studentWithThisEmail.getSingleResult();
    }
}
