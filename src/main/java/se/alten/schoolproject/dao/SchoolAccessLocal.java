package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents() throws Exception;

    StudentModel addStudent(String studentModel);

    void removeStudent(String student);

    void updateStudent(String forename, String lastname, String email);

    void updateStudentPartial(String studentModel);

    List findStudentByName(String forename);

    StudentModel findStudentByEmail(String email);

    List listAllSubjects();

    SubjectModel addSubject(String subjectModel);

    void removeSubject(Long id);

    void updateSubject(Long id, String subject) throws Exception;

    List<Teacher> listAllTeachers();

    TeacherModel addTeacher(String teacherModel);

    void removeTeacher(String email);

    void updateTeacher(String firstName, String lastName, String email);
}
