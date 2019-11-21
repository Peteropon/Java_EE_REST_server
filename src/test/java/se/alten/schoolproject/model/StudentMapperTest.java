package se.alten.schoolproject.model;

import org.junit.Test;
import se.alten.schoolproject.entity.Student;

import static org.junit.Assert.*;

public class StudentMapperTest {

    @Test
    public void shouldMapStudentEntityToStudentModel() {
        //given
        Student tim = new Student(2L, "tim", "tom", "email");

        //when
        StudentModel timModel = StudentMapper.INSTANCE.toStudentModel(tim);

        //then
        assertNotNull(timModel);
        assertEquals(timModel.getEmail(), "email");
        assertEquals(timModel.getForename(), "tim");
        assertEquals(timModel.getLastname(), "tom");
        assertEquals(timModel.getClass(), StudentModel.class);
    }
}