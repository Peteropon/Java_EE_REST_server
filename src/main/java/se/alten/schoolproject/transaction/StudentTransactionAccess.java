package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    Student addStudent(Student studentToAdd);
    void removeStudent(String student);
    void updateStudent(String forename, String lastName, String email);
    void updateStudentPartial(Student studentToUpdate);

    List findStudentByName(String forename);

    Student findStudentByEmail(String email);
}
