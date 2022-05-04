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

			Member member1= entityManager.find(Member.class, 1L);
			Member member2= entityManager.find(Member.class, 1L);

			System.out.println("member1 = " + member1.toString());
			System.out.println("member2 = " + member2.toString());
			System.out.println(member1 == member2);

			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
		}finally {
			entityManager.close();
		}

		entityManagerFactory.close();
	}
}
