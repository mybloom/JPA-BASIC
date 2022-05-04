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
			List<Member> members = entityManager.createQuery("select m from Member as m",
					Member.class)
				.setFirstResult(5)
				.setMaxResults(8)
				.getResultList();

			for (Member member : members) {
				System.out.println("member.name = " + member.getName());
			}

			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
		}finally {
			entityManager.close();
		}

		entityManagerFactory.close();
	}
}
