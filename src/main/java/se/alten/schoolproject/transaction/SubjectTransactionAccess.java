package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Subject;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List listAllSubjects();
    Subject addSubject(Subject subject);
    List<Subject> getSubjectByName(List<String> subject);
    void removeSubject(Long id);
    void updateSubject(Long id, String subject);
    Subject findById(Long id);

    void addStudentToSubject(Subject subjectToUpdate);
}
