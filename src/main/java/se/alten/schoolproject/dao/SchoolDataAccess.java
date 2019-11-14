package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;

import javax.ejb.NoSuchEntityException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();
    private Subject subject = new Subject();
    private SubjectModel subjectModel = new SubjectModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Override
    public List listAllStudents(){
        List<StudentModel> sm = studentModel.toModelList(studentTransactionAccess.listAllStudents());
        return sm;
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
        return subjectTransactionAccess.listAllSubjects();
    }

    @Override
    public SubjectModel addSubject(String newSubject) {
        Subject subjectToAdd = subject.toEntity(newSubject);
        subjectTransactionAccess.addSubject(subjectToAdd);
        return subjectModel.toModel(subjectToAdd);
    }
}
