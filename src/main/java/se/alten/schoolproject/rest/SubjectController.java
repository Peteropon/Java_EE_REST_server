package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.model.SubjectModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.undertow.util.StatusCodes.UNPROCESSABLE_ENTITY;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Stateless
@NoArgsConstructor
@Path("/subject")
public class SubjectController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response listSubjects() {
        try {
            List subject = sal.listAllSubjects();
            return Response.ok(subject).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Produces({"application/JSON"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSubject(String subject) {
        try {
            SubjectModel subjectModel = sal.addSubject(subject);
            return Response.ok(subjectModel).build();
        } catch (Exception e ) {
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser( @PathParam("id") Long id) {
        try {
            sal.removeSubject(id);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @PUT
    public Response updateSubject(@QueryParam("id") Long id, @QueryParam("subject") String subjectModel) {
        try {
            sal.updateSubject(id, subjectModel);
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("{id}/student/{email}")
    public Response addStudentToSubject(@PathParam("id") Long id, @PathParam("email") String email) {
        try {
            sal.addStudentToSubject(id, email);
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(UNPROCESSABLE_ENTITY).entity(e.getMessage()).build();
        }
    }
}
