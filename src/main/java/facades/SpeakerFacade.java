package facades;

import dtos.SpeakerDTO;
import entities.Speaker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class SpeakerFacade {

    private static SpeakerFacade instance;
    private static EntityManagerFactory emf;

    private SpeakerFacade() {
    }

    public static SpeakerFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SpeakerFacade();
        }
        return instance;
    }

    public List<SpeakerDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Speaker> query = em.createQuery("SELECT s FROM Speaker s", Speaker.class);
            List<Speaker> speakers = query.getResultList();
            return SpeakerDTO.getDTOs(speakers);
        }
        finally {
            em.close();
        }
    }

    public SpeakerDTO getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Speaker speaker = em.find(Speaker.class, id);
            if (speaker == null) {
                throw new WebApplicationException("Speaker not found", 404);
            }
            return new SpeakerDTO(speaker);
        }
        finally {
            em.close();
        }
    }
}
