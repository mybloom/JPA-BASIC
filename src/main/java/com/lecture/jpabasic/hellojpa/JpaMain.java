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
			//영속
			Member member1 = entityManager.find(Member.class, 1L);
			member1.setName("AAA");

			entityManager.detach(member1);
			System.out.println("=======");

			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
		}finally {
			entityManager.close();
		}

		entityManagerFactory.close();
	}
}
