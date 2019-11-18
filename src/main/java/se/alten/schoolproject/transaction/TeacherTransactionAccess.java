package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Teacher;

import javax.ejb.Local;
import java.util.List;

@Local
public interface TeacherTransactionAccess {
    List<Teacher> listAllTeachers();
    Teacher addTeacher(Teacher teacher);
    void removeTeacher(String teacher);
    void updateTeacher(String forename, String lastName, String email);

}
