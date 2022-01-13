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
        Conference c2 = new Conference("ComicCon", "San Diego", 20000, LocalDate.now().plusMonths(6), LocalTime.now());
        Conference c3 = new Conference("PAX East", "Boston", 35000, LocalDate.now().plusWeeks(3), LocalTime.now().plusHours(2));
        Talk t1 = new Talk("Public Relations", "2 hours", c1);
        Talk t2 = new Talk("Ethics of Vigilantism", "3 hours", c2);
        Talk t3 = new Talk("Ubisoft", "30 minutes", c3);
        Speaker s1 = new Speaker("Testy Testson", "tester", "male");
        Speaker s2 = new Speaker("Erica Spokesman", "presenter", "female");
        Speaker s3 = new Speaker("Peter Parker", "vigilante", "male");
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

    public static void main(String[] args) {
        populate();
    }
}
