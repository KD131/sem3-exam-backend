package rest;

import dtos.SpeakerDTO;
import entities.Role;
import entities.Speaker;
import entities.User;
import facades.SpeakerFacade;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class SpeakerResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static Speaker s1;
    private static Speaker s2;
    private static Speaker s3;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        s1 = new Speaker("Testy Testson", "tester", "male");
        s2 = new Speaker("Erica Spokesman", "presenter", "female");
        s3 = new Speaker("Peter Parker", "vigilante", "male");
        try {
            em.getTransaction().begin();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);

            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            em.createNativeQuery("DELETE FROM speakers_talks").executeUpdate();
            em.createNativeQuery("DELETE FROM talks").executeUpdate();
            em.createNativeQuery("DELETE FROM speakers").executeUpdate();
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    void create() {
        login("admin", "test");
        SpeakerDTO newSpeaker = new SpeakerDTO("New Guy", "intern", "male");

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .body(newSpeaker)
                .when()
                .post("speakers")
                .then()
                .statusCode(200)
                .body("name", equalTo(newSpeaker.getName()))
                .body("profession", equalTo(newSpeaker.getProfession()))
                .body("gender", equalTo(newSpeaker.getGender()));
    }

    @Test
    void update() {
        login("admin", "test");
        SpeakerDTO newItem = new SpeakerDTO(s2);
        newItem.setProfession("skater boi");

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .body(newItem)
                .when()
                .put("speakers/id/" + newItem.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(newItem.getName()))
                .body("profession", equalTo(newItem.getProfession()))
                .body("gender", equalTo(newItem.getGender()));
    }

    @Test
    void update_ignoreBadId() {
        login("admin", "test");
        SpeakerDTO newItem = new SpeakerDTO(s2);
        newItem.setProfession("skater boi");
        newItem.setId(99);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .body(newItem)
                .when()
                .put("speakers/id/" + s2.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo(newItem.getName()))
                .body("profession", equalTo(newItem.getProfession()))
                .body("gender", equalTo(newItem.getGender()));
    }
}