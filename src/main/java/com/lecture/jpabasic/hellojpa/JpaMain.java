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

			Member member1 = new Member();
			member1.setUserName("member1");
			entityManager.persist(member1);

			Team team = new Team();
			team.setName("TeamA");
			team.getMembers().add(member1);

			entityManager.persist(team);

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
