package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.transaction.TeacherTransactionAccess;

import javax.ejb.NoSuchEntityException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();
    private Subject subject = new Subject();
    private SubjectModel subjectModel = new SubjectModel();
    private Teacher teacher = new Teacher();
    private TeacherModel teacherModel = new TeacherModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Inject
    TeacherTransactionAccess teacherTransactionAccess;

    @Override
    public List<StudentModel> listAllStudents(){
        List<Student> studentList = studentTransactionAccess.listAllStudents();
        return studentModel.toModelList(studentList);
    }

    @Override
    public StudentModel addStudent(String newStudent) throws IllegalArgumentException {
        Student studentToAdd = student.toEntity(newStudent);
        boolean checkForEmptyVariables = Stream.of(studentToAdd.getForename(), studentToAdd.getLastName(), studentToAdd.getEmail()).anyMatch(String::isBlank);

        if (checkForEmptyVariables) {
            studentToAdd.setForename("empty");
            return studentModel.toModel(studentToAdd);
        } else {
           studentTransactionAccess.addStudent(studentToAdd);

            List<Subject> subjects = subjectTransactionAccess.getSubjectByName(studentToAdd.getSubjects());

            subjects.forEach(sub -> {
                studentToAdd.getSubject().add(sub);
            });

            return studentModel.toModel(studentToAdd);
        }
    }

    @Override
    public void removeStudent(String studentEmail) throws NoSuchEntityException {
        if (findStudentByEmail(studentEmail) == null) throw new NoSuchEntityException();
        else {
            studentTransactionAccess.removeStudent(studentEmail);
        }
    }

    @Override
    public void updateStudent(String forename, String lastName, String email) throws NoSuchEntityException{
        if (findStudentByEmail(email) == null) {
            throw new NoSuchEntityException();
        }
        else studentTransactionAccess.updateStudent(forename, lastName, email);
    }

    @Override
    public void updateStudentPartial(String studentModel) throws IllegalArgumentException, NoSuchEntityException{
        Student studentToUpdate = student.toEntity(studentModel);
        if (findStudentByEmail(studentToUpdate.getEmail()) == null) throw new NoSuchEntityException();
        if (studentToUpdate.getForename().equals("")
                || studentToUpdate.getLastName().equals("")) throw new IllegalArgumentException();
        else studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }

    @Override
    public List findStudentByName(String forename) throws NoResultException {
        List namesAsStudent = studentTransactionAccess.findStudentByName(forename);
        List<StudentModel> namesAsModel = new ArrayList<>();
        if (namesAsStudent.size() == 0) throw new NoResultException();
        else {
            for (int i = 0; i < namesAsStudent.size(); i++) {
                namesAsModel.add(studentModel.toModel((Student) namesAsStudent.get(i)));
            }
            return namesAsModel;
        }
    }

    @Override
    public StudentModel findStudentByEmail(String email) throws NoResultException {
        Student studentToShow = studentTransactionAccess.findStudentByEmail(email);
        if (studentToShow == null) throw new NoResultException();
        else return studentModel.toModel(studentToShow);
    }

    @Override
    public List listAllSubjects() {
        List subjectList = subjectTransactionAccess.listAllSubjects();
        List<SubjectModel> subjectModels = new ArrayList<>();
        subjectList.forEach(subject -> {
            subjectModels.add(subjectModel.toModel((Subject) subject));
        });
        return subjectModels;
    }

    @Override
    public SubjectModel addSubject(String newSubject) {
        Subject subjectToAdd = subject.toEntity(newSubject);
        subjectTransactionAccess.addSubject(subjectToAdd);
        return subjectModel.toModel(subjectToAdd);
    }

    @Override
    public void removeSubject(Long id) {
        subjectTransactionAccess.removeSubject(id);
    }

    @Override
    public void updateSubject(Long id, String subject) throws Exception {
        Subject subjectToUpdate = subjectTransactionAccess.findById(id);
        if (subjectToUpdate == null) throw new NoSuchEntityException();
        else subjectTransactionAccess.updateSubject(id, subject);
    }

    @Override
    public void addStudentToSubject(Long id, String email) {
        Subject subjectToUpdate = subjectTransactionAccess.findById(id);
        Student studentToUpdate = studentTransactionAccess.findStudentByEmail(email);
        subjectToUpdate.addStudent(studentToUpdate);
        studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }

    @Override
    public void addTeacherToSubject(Long id, String email) {

    }

    @Override
    public List<TeacherModel> listAllTeachers() {
        List<Teacher> test = teacherTransactionAccess.listAllTeachers();
        List<TeacherModel> teachersList = new ArrayList<>();
        test.forEach(t -> {
            teachersList.add(teacherModel.toModel(t));
        });
        return teachersList;
    }

    @Override
    public TeacherModel addTeacher(String newTeacher) {
        Teacher teacherToAdd = teacher.toEntity(newTeacher);
        boolean checkForEmptyVariables = Stream.of(teacherToAdd.getTeacherFirstName(), teacherToAdd.getTeacherLastName(), teacherToAdd.getTeacherEmail()).anyMatch(String::isBlank);

        if (checkForEmptyVariables) {
            teacherToAdd.setTeacherFirstName("empty");
            return teacherModel.toModel(teacherToAdd);
        } else {
            teacherTransactionAccess.addTeacher(teacherToAdd);

            List<Subject> subjects = subjectTransactionAccess.getSubjectByName(teacherToAdd.getSubjects());

            subjects.forEach(sub -> {
                teacherToAdd.getSubject().add(sub);
            });

            return teacherModel.toModel(teacherToAdd);
        }
    }

    @Override
    public void removeTeacher(String email) {
        teacherTransactionAccess.removeTeacher(email);
//        if (fin(studentEmail) == null) throw new NoSuchEntityException();
//        else {
//            studentTransactionAccess.removeStudent(studentEmail);
//        }
    }

    @Override
    public void updateTeacher(String firstName, String lastName, String email) {
        teacherTransactionAccess.updateTeacher(firstName, lastName, email);
    }
}
