package facades;

import dtos.ConferenceDTO;
import entities.Conference;
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

class ConferenceFacadeTest {

    private static EntityManagerFactory emf;
    private static ConferenceFacade facade;

    private static Conference c1;
    private static Conference c2;
    private static Conference c3;

    @BeforeAll
    static void beforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ConferenceFacade.getFacade(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();

        c1 = new Conference("TestCon", "Test City", 10, LocalDate.now(), LocalTime.now());
        c2 = new Conference("ComicCon", "San Diego", 20000, LocalDate.now().plusMonths(6), LocalTime.now());
        c3 = new Conference("PAX East", "Boston", 35000, LocalDate.now().plusWeeks(3), LocalTime.now().plusHours(2));

        try {
            em.getTransaction().begin();
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
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
            em.createNativeQuery("DELETE FROM conferences").executeUpdate();
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
        ConferenceDTO dto = facade.getById(c1.getId());
        assertEquals(c1.getName(), dto.getName());
    }

    @Test
    void getById_badId() {
        WebApplicationException e = assertThrows(WebApplicationException.class, () -> {
            facade.getById(666);
        });
        assertEquals(404, e.getResponse().getStatus());
        assertEquals("Conference not found", e.getMessage());
    }
}