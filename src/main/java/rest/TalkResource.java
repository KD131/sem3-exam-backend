package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.TalkFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("talks")
public class TalkResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final TalkFacade TALK_FACADE = TalkFacade.getFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    @RolesAllowed({"user", "admin"})
    public String getAll() {
        return GSON.toJson(TALK_FACADE.getAll());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("id/{id}")
    @RolesAllowed({"user", "admin"})
    public String getById(@PathParam("id") int id) {
        return GSON.toJson(TALK_FACADE.getById(id));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("id/{id}")
    @RolesAllowed({"admin"})
    public String delete(@PathParam("id") int id) {
        return GSON.toJson(TALK_FACADE.delete(id));
    }
}
