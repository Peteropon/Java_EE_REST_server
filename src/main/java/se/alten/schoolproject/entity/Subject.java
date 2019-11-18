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

    @JsonIgnore
    @ManyToMany(mappedBy = "subject", cascade = PERSIST, fetch = LAZY)
    private Set<Teacher> teacher = new HashSet<>();

    @Transient
    private List<String> studentList = new ArrayList<>();

    public Subject toEntity(String subjectModel) {
        JsonReader reader = Json.createReader(new StringReader(subjectModel));

        JsonObject jsonObject = reader.readObject();

        Subject subject = new Subject();

        List<String> temp = new ArrayList<>();

        if ( jsonObject.containsKey("subject")) {

            subject.setTitle(jsonObject.getString("subject"));
        } else {
            subject.setTitle("");
        }

        if (jsonObject.containsKey("students")) {
            JsonArray jsonArray = jsonObject.getJsonArray("students");
            for (int i = 0; i < jsonArray.size(); i++) {
                temp.add(jsonArray.get(i).toString().replace("\"", ""));
                subject.setStudentList(temp);
            }
            subject.setStudentList(studentList);
        } else subject.setStudentList(null);

        return subject;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.getSubject().add(this);
    }
}
