package utils;

import entities.Conference;
import entities.Speaker;
import entities.Talk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalTime;

public class Populator {
    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        Conference c1 = new Conference("TestCon", "Test City", 10, LocalDate.now(), LocalTime.now());
        Talk t1 = new Talk("Public Relations", "2 hours", c1);
        Speaker s1 = new Speaker("Testy Testson", "male");
        t1.addSpeaker(s1);

        try {
            em.getTransaction().begin();
            em.persist(c1);
            em.persist(t1);
            em.persist(s1);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        populate();
    }
}
