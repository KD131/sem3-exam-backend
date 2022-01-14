package rest;

import utils.Populator;
import utils.SetupTestUsers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("populate")
public class PopulateResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("users")
    public String testUsers() {
        SetupTestUsers.populate();
        return "Successfully populated with test users.";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("data")
    public String testData() {
        Populator.populate();
        return "Successfully populated with test data.";
    }
}
