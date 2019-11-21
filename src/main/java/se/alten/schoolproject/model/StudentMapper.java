package se.alten.schoolproject.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import se.alten.schoolproject.entity.Student;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(ignore = true, target = "id")
    StudentModel toStudentModel(Student student);
}
