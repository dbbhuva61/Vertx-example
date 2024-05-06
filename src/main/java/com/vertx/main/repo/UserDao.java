package com.vertx.main.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.vertx.main.entity.User;



public class UserDao {

	
	private static UserDao instance;
	protected EntityManager entityManager;
	
	
	public static UserDao getInstance() {
		if(instance == null) {
			 instance = new UserDao();
		}
		return instance;
	}
		
	private UserDao() {
		entityManager = getEntityManager();
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudHibernatePU");
		
		if(entityManager == null) {
			entityManager = factory.createEntityManager();
		}
		return null;
	}
	
	public User getById(String id) {
        Object result = entityManager.find(User.class, id);
        
        if (result != null) {
            return (User) result;
        } else {
            return null;
        }
    }
	
	public List<User> findall(){
		
		return entityManager.createQuery("FROM " + User.class.getName()).getResultList();
	}
	
	public void presist(User user) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.getTransaction().commit();
			
		} catch (Exception e) {
			 e.printStackTrace();
	         entityManager.getTransaction().rollback();
		}
	}
	
	public void merge(User user) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(user);
			entityManager.getTransaction().commit();
			
		} catch (Exception e) {
			 e.printStackTrace();
	         entityManager.getTransaction().rollback();
		}
	}	
	
	public void remove(User user) {
		try {
			entityManager.getTransaction().begin();
			user = entityManager.find(User.class, user.getId());
			entityManager.remove(user);
			entityManager.getTransaction().commit();
			
		} catch (Exception e) {
			 e.printStackTrace();
	         entityManager.getTransaction().rollback();
		}
	}
	
	public void removeById(String id) {
		try {
			User user = getById(id);
			remove(user);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}		
}
