package rest;

import utils.SetupTestUsers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("populate")
public class PopulateResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testUsers() {
        SetupTestUsers.populate();
        return "Successfully populated with test users.";
    }
}
