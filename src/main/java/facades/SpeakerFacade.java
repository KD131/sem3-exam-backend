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

    public SpeakerDTO create(SpeakerDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            Speaker entity = new Speaker(dto);
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return new SpeakerDTO(entity);
        }
        finally {
            em.close();
        }
    }

    public SpeakerDTO update(SpeakerDTO dto) {
        EntityManager em = emf.createEntityManager();
        try {
            Speaker oldItem = em.find(Speaker.class, dto.getId());
            if (oldItem == null) {
                throw new WebApplicationException("Speaker not found", 404);
            }
            Speaker newItem = new Speaker(dto);
            newItem.setId(oldItem.getId());     // I'm not sure why this is needed. I thought they already had the same ID.
            em.getTransaction().begin();
            em.merge(newItem);
            em.getTransaction().commit();
            return new SpeakerDTO(newItem);
        }
        finally {
            em.close();
        }
    }
}
