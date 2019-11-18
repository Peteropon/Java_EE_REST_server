package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Teacher;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherModel implements Serializable {

    private String teacherFirstName;
    private String teacherLastName;
    private String teacherEmail;
    private Set<String> subjectsForTeacher = new HashSet<>();
    private Set<String> studentsForTeacher = new HashSet<>();

    public TeacherModel toModel(Teacher teacherToAdd) {
        TeacherModel teacherModel = new TeacherModel();
        switch (teacherToAdd.getTeacherFirstName()) {
            case "empty":
                teacherModel.setTeacherFirstName("empty");
                return teacherModel;
            case "duplicate":
                teacherModel.setTeacherFirstName("duplicate");
                return teacherModel;
            default:
                teacherModel.setTeacherFirstName(teacherToAdd.getTeacherFirstName());
                teacherModel.setTeacherLastName(teacherToAdd.getTeacherLastName());
                teacherModel.setTeacherEmail(teacherToAdd.getTeacherEmail());
                teacherToAdd.getSubject().forEach(subject -> {
                    teacherModel.subjectsForTeacher.add(subject.getTitle());
                });
                return teacherModel;
        }
    }

}
