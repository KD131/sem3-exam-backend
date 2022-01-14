package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.SpeakerDTO;
import facades.SpeakerFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String speaker) {
        SpeakerDTO toCreate = GSON.fromJson(speaker, SpeakerDTO.class);
        SpeakerDTO created = SPEAKER_FACADE.create(toCreate);
        return GSON.toJson(created);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("id/{id}")
    public String update(@PathParam("id") int id, String speaker) {
        SpeakerDTO toUpdate = GSON.fromJson(speaker, SpeakerDTO.class);
        // we ignore bad Ids in the provided JSON
        toUpdate.setId(id);
        SpeakerDTO updated = SPEAKER_FACADE.update(toUpdate);
        return GSON.toJson(updated);
    }

}