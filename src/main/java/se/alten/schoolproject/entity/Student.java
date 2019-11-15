package se.alten.schoolproject.entity;

import lombok.*;
import javax.json.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.io.StringReader;
import java.util.*;

@Entity
@Table(name="student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "forename")
    @NotNull
    @NotEmpty
    private String forename;

    @Column(name = "lastname")
    @NotNull
    @NotEmpty
    private String lastName;

    @Column(name = "email", unique = true)
    @NotNull
    @NotEmpty
    private String email;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "student_subject",
                joinColumns=@JoinColumn(name="stud_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "subj_id", referencedColumnName = "id"))
    private Set<Subject> subject = new HashSet<>();

    @Transient
    private List<String> subjects = new ArrayList<>();

    public Student toEntity(String studentModel) {

        List<String> temp = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(studentModel));

        JsonObject jsonObject = reader.readObject();

        Student student = new Student();
        if ( jsonObject.containsKey("forename")) {
            student.setForename(jsonObject.getString("forename"));
        } else {
            student.setForename("");
        }

        if ( jsonObject.containsKey("lastname")) {
            student.setLastName(jsonObject.getString("lastname"));
        } else {
            student.setLastName("");
        }

        if ( jsonObject.containsKey("email")) {
            student.setEmail(jsonObject.getString("email"));
        } else {
            student.setEmail("");
        }

        if (jsonObject.containsKey("subject")) {
            JsonArray jsonArray = jsonObject.getJsonArray("subject");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                temp.add(jsonArray.get(i).toString().replace("\"", ""));
                student.setSubjects(temp);
            }
        } else {
            student.setSubjects(null);
        }

        return student;
    }
}
