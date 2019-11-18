package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.model.TeacherModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.undertow.util.StatusCodes.UNPROCESSABLE_ENTITY;
import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Stateless
@NoArgsConstructor
@Path("/teacher")
public class TeacherController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showTeachers() {
        try {
            List<Teacher> teachers = sal.listAllTeachers();
            teachers.forEach(t -> {
                System.out.println(t.toString());
            });
            return Response.ok(teachers).build();
        } catch ( Exception e ) {
            return Response.status(NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response addTeacher(String newTeacher) {
        try {
            TeacherModel postAnswer = sal.addTeacher(newTeacher);

            switch ( postAnswer.getTeacherFirstName()) {
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
    public Response deleteTeacher( @PathParam("email") String email) {
        try {
            sal.removeTeacher(email);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @PUT
    public Response updateTeacher(@QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) {
        try {
            sal.updateTeacher(forename, lastname, email);
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }

}
