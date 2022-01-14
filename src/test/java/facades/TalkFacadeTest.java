package facades;

import entities.Conference;
import entities.Speaker;
import entities.Talk;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TalkFacadeTest {

    private static EntityManagerFactory emf;
    private static TalkFacade facade;

    private static Conference c1;
    private static Conference c2;
    private static Conference c3;
    private static Talk t1;
    private static Talk t2;
    private static Talk t3;
    private static Speaker s1;
    private static Speaker s2;
    private static Speaker s3;

    @BeforeAll
    static void beforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = TalkFacade.getFacade(emf);
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

    @Test
    void delete() {
        assertEquals(3, facade.getAll().size());
        facade.delete(t2.getId());
        assertEquals(2, facade.getAll().size());
    }

    @Test
    void delete_badId() {
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            facade.delete(666);
        });
        assertEquals(404, e.getResponse().getStatus());
        assertEquals("Talk not found", e.getMessage());
    }
}