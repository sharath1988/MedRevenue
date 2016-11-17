/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;


/**
 * Producer for the default EntityManager (EE-Server):
 * 
 * @author somdev5
 *
 */
@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @Default
    @RequestScoped
    public EntityManager create()
    {
        return this.entityManagerFactory.createEntityManager();
    }

    public void dispose(@Disposes @Default EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }

}
