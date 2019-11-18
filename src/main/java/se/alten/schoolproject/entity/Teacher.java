package se.alten.schoolproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Teacher {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String teacherFirstName;

    @Column
    private String teacherLastName;

    @Column
    private String teacherEmail;

    @ManyToMany
    private Set<Subject> subject = new HashSet<>();

    @Transient
    private List<String> subjects = new ArrayList<>();

    public Teacher toEntity(String teacherModel) {
        List<String> temp = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(teacherModel));

        JsonObject jsonObject = reader.readObject();

        Teacher teacher = new Teacher();
        if ( jsonObject.containsKey("forename")) {
            teacher.setTeacherFirstName(jsonObject.getString("forename"));
        } else {
            teacher.setTeacherFirstName("");
        }

        if ( jsonObject.containsKey("lastname")) {
            teacher.setTeacherLastName(jsonObject.getString("lastname"));
        } else {
            teacher.setTeacherLastName("");
        }

        if ( jsonObject.containsKey("email")) {
            teacher.setTeacherEmail(jsonObject.getString("email"));
        } else {
            teacher.setTeacherEmail("");
        }

        if (jsonObject.containsKey("subject")) {
            JsonArray jsonArray = jsonObject.getJsonArray("subject");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                temp.add(jsonArray.get(i).toString().replace("\"", ""));
                teacher.setSubjects(temp);
            }
        } else {
            teacher.setSubjects(null);
        }

        return teacher;
    }
}
