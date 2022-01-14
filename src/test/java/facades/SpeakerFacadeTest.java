package facades;

import dtos.ConferenceDTO;
import dtos.SpeakerDTO;
import entities.Speaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import static org.junit.jupiter.api.Assertions.*;

class SpeakerFacadeTest {

    private static EntityManagerFactory emf;
    private static SpeakerFacade facade;

    private static Speaker s1;
    private static Speaker s2;
    private static Speaker s3;

    @BeforeAll
    static void beforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = SpeakerFacade.getFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        s1 = new Speaker("Testy Testson", "tester", "male");
        s2 = new Speaker("Erica Spokesman", "presenter", "female");
        s3 = new Speaker("Peter Parker", "vigilante", "male");
        try {
            em.getTransaction().begin();
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
            em.createNativeQuery("DELETE FROM talks").executeUpdate();
            em.createNativeQuery("DELETE FROM speakers").executeUpdate();
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    @Test
    void getAll() {
        assertEquals(3, facade.getAll().size());
    }

    @Test
    void getById() {
        SpeakerDTO dto = facade.getById(s1.getId());
        assertEquals(s1.getName(), dto.getName());
    }

    @Test
    void getById_badId() {
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            facade.getById(666);
        });
        assertEquals(404, e.getResponse().getStatus());
        assertEquals("Speaker not found", e.getMessage());
    }

    @Test
    void create() {
        assertEquals(3, facade.getAll().size());
        SpeakerDTO newSpeaker = new SpeakerDTO("New Guy", "intern", "male");
        facade.create(newSpeaker);
        assertEquals(4, facade.getAll().size());
    }
}