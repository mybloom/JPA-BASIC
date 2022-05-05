package com.lecture.jpabasic.hellojpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		try {
			Movie movie = new Movie();
			movie.setDirector("감독님");
			movie.setActor("배우");
			movie.setName("바람과함께 사라지다");
			movie.setPrice(10000);

			entityManager.persist(movie);

			entityManager.flush();
			entityManager.clear();

			Item item = entityManager.find(Item.class, movie.getId());
			System.out.println("item = " + item);

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			entityManager.close();
		}

		entityManagerFactory.close();
	}
}
