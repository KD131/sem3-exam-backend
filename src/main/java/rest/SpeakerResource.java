package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.SpeakerFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("speakers")
public class SpeakerResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final SpeakerFacade SPEAKER_FACADE = SpeakerFacade.getFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    @RolesAllowed({"user", "admin"})
    public String getAll() {
        return GSON.toJson(SPEAKER_FACADE.getAll());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("id/{id}")
    @RolesAllowed({"user", "admin"})
    public String getById(@PathParam("id") int id) {
        return GSON.toJson(SPEAKER_FACADE.getById(id));
    }
}