package se.alten.schoolproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty
    private String title;

    @JsonIgnore
    @ManyToMany(mappedBy = "subject", cascade = PERSIST, fetch = LAZY)
    private Set<Student> students = new HashSet<>();

    @ManyToMany
    @JoinColumn
    private Set<Teacher> teacher = new HashSet<>();

    @Transient
    private List<String> studentList = new ArrayList<>();

    public Subject toEntity(String subjectModel) {
        JsonReader reader = Json.createReader(new StringReader(subjectModel));

        JsonObject jsonObject = reader.readObject();

        Subject subject = new Subject();

        if ( jsonObject.containsKey("subject")) {

            subject.setTitle(jsonObject.getString("subject"));
        } else {
            subject.setTitle("");
        }

//        if (jsonObject.containsKey("students")) {
//            JsonArray jsonArray = jsonObject.getJsonArray("students");
//            for (javax.json.JsonValue jsonValue : jsonArray) {
//                studentList.add(jsonValue.toString().replace("\"", ""));
//            }
//            subject.setStudentList(studentList);
//        }

        return subject;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.getSubject().add(this);
    }
}
