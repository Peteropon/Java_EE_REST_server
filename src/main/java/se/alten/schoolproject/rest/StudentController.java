package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.undertow.util.StatusCodes.UNPROCESSABLE_ENTITY;
import static javax.ws.rs.core.Response.Status.*;

@Stateless
@NoArgsConstructor
@Path("/student")
public class StudentController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showStudents() {
        try {
            List students = sal.listAllStudents();
            return Response.ok(students).build();
        } catch ( Exception e ) {
            return Response.status(NOT_FOUND).build();
        }
    }

    @GET
    @Path("/emails")
    @Produces({"application/JSON"})
    public Response showStudentByEmail(@QueryParam("email") String email) {
        try {
            StudentModel searchResult = sal.findStudentByEmail(email);
            return Response.ok(searchResult).build();
        } catch (Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/names")
    @Produces({"application/JSON"})
    public Response showStudentByName(@QueryParam("forename") String forename) {
        try {
            List student = sal.findStudentByName(forename);
            return Response.ok(student).build();
        } catch ( Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    /**
     * JavaDoc
     */
    public Response addStudent(String studentModel) {
        try {
            StudentModel postAnswer = sal.addStudent(studentModel);

            switch ( postAnswer.getForename()) {
                case "empty":
                    return Response.status(NOT_ACCEPTABLE).entity("{\"Fill in all details please\"}").build();
                case "duplicate":
                    return Response.status(CONFLICT).entity("{\"Email already registered!\"}").build();
                default:
                    return Response.ok(postAnswer).build();
            }
        } catch ( Exception e ) {
            return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{email}")
    public Response deleteUser( @PathParam("email") String email) {
        try {
            sal.removeStudent(email);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @PUT
    public Response updateStudent(@QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) {
        try {
            sal.updateStudent(forename, lastname, email);
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }

    @PATCH
    public Response updatePartialAStudent(String studentModel) {
        try {
            sal.updateStudentPartial(studentModel);
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }
}
