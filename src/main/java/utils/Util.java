package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Util {

    private static final String NAME = "Arhivator";
    private  EntityManagerFactory emFatory;
    private  EntityManager em;

    public Util(){
        emFatory = Persistence.createEntityManagerFactory(NAME);
        em = emFatory.createEntityManager();
    }

    public void close(){
        if(em != null)  em.close();
        if(emFatory != null) emFatory.close();
    }


    public EntityManager getEm() {
        return em;
    }
}
