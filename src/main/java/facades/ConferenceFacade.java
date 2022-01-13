package facades;

import dtos.ConferenceDTO;
import entities.Conference;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class ConferenceFacade {

    private static ConferenceFacade instance;
    private static EntityManagerFactory emf;

    private ConferenceFacade() {
    }

    public static ConferenceFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ConferenceFacade();
        }
        return instance;
    }

    public List<ConferenceDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Conference> query = em.createQuery("SELECT c FROM Conference c", Conference.class);
            List<Conference> conferences = query.getResultList();
            return ConferenceDTO.getDTOs(conferences);
        }
        finally {
            em.close();
        }
    }
}
