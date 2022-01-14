package facades;

import dtos.TalkDTO;
import entities.Talk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class TalkFacade {

    private static TalkFacade instance;
    private static EntityManagerFactory emf;

    private TalkFacade() {
    }

    public static TalkFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TalkFacade();
        }
        return instance;
    }

    public List<TalkDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Talk> query = em.createQuery("SELECT t FROM Talk t", Talk.class);
            List<Talk> talks = query.getResultList();
            return TalkDTO.getDTOs(talks);
        }
        finally {
            em.close();
        }
    }

    public TalkDTO getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Talk talk = em.find(Talk.class, id);
            if (talk == null) {
                throw new WebApplicationException("Talk not found", 404);
            }
            return new TalkDTO(talk);
        }
        finally {
            em.close();
        }
    }

    public TalkDTO delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Talk talk = em.find(Talk.class, id);
            if (talk == null) {
                throw new WebApplicationException("Talk not found", 404);
            }
            em.getTransaction().begin();
            talk.removeAllSpeakers();
            talk.getConference().removeTalk(talk);
            em.remove(talk);
            em.getTransaction().commit();
            return new TalkDTO(talk);
        }
        finally {
            em.close();
        }
    }
}
