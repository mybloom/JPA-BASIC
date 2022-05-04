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
			Member member1 =  new Member(3L, "member3");
			Member member2 =  new Member(4L, "member4");

			entityManager.persist(member1);
			entityManager.persist(member2);
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
