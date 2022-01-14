package rest;

import com.google.gson.Gson;
import entities.*;
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
import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class TalkResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static Gson gson = new Gson();

    private static Conference c1;
    private static Conference c2;
    private static Conference c3;
    private static Talk t1;
    private static Talk t2;
    private static Talk t3;
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

        c1 = new Conference("TestCon", "Test City", 10, LocalDate.now(), LocalTime.now());
        c2 = new Conference("ComicCon", "San Diego", 20000, LocalDate.now().plusMonths(6), LocalTime.now());
        c3 = new Conference("PAX East", "Boston", 35000, LocalDate.now().plusWeeks(3), LocalTime.now().plusHours(2));
        t1 = new Talk("Public Relations", "2 hours", c1);
        t2 = new Talk("Ethics of Vigilantism", "3 hours", c2);
        t3 = new Talk("Ubisoft", "30 minutes", c3);
        s1 = new Speaker("Testy Testson", "tester", "male");
        s2 = new Speaker("Erica Spokesman", "presenter", "female");
        s3 = new Speaker("Peter Parker", "vigilante", "male");
        t1.addSpeaker(s1);
        t2.addSpeaker(s3);
        t3.addSpeaker(s2);
        t3.addSpeaker(s3);

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

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(t1);
            em.persist(t2);
            em.persist(t3);
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
            em.createNativeQuery("DELETE FROM speakers").executeUpdate();
            em.createNativeQuery("DELETE FROM talks").executeUpdate();
            em.createNativeQuery("DELETE FROM conferences").executeUpdate();
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
    void getAll() {
        login("user", "test");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("talks/all")
                .then()
                .statusCode(200)
                .body("size", equalTo(3));
    }

    @Test
    void getById() {
        login("user", "test");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("talks/id/" + t2.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(t2.getId()))
                .body("topic", equalTo(t2.getTopic()));
    }

    @Test
    void getById_badId() {
        login("user", "test");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("talks/id/" + 99)
                .then()
                .body("code", equalTo(404))
                .body("message", equalTo("Talk not found"));
    }

    @Test
    void delete() {
        login("admin", "test");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("talks/id/" + t3.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(t3.getId()))
                .body("topic", equalTo(t3.getTopic()));
    }

    @Test
    void delete_badId() {
        login("admin", "test");
        given()
                .accept(MediaType.APPLICATION_JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("talks/id/" + 99)
                .then()
                .body("code", equalTo(404))
                .body("message", equalTo("Talk not found"));
    }
}