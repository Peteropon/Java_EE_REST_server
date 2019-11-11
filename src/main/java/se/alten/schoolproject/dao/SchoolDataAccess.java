package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.NoSuchEntityException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List listAllStudents(){
        return studentTransactionAccess.listAllStudents();
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
        else {
            studentTransactionAccess.updateStudent(forename, lastName, email);
        }
    }

    @Override
    public void updateStudentPartial(String studentModel) throws IllegalArgumentException, NoSuchEntityException{
        Student studentToUpdate = student.toEntity(studentModel);
        if (findStudentByEmail(studentToUpdate.getEmail()) == null) throw new NoSuchEntityException();
        if (studentToUpdate.getForename().equals("") ) throw new IllegalArgumentException();
        else studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }

    @Override
    public List findStudentByName(String forename) {
        List studentsWithName = studentTransactionAccess.findStudentByName(forename);
        return studentsWithName;
    }

    @Override
    public StudentModel findStudentByEmail(String email) throws NoResultException {
        Student studentToShow = studentTransactionAccess.findStudentByEmail(email);
        if (studentToShow == null) throw new NoResultException();
        else return studentModel.toModel(studentToShow);
    }
}
