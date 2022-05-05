package com.lecture.jpabasic.hellojpa;

import java.time.LocalDateTime;
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
			Member member = new Member();
			member.setUserName("사용자");
			member.setCreatedBy("홍길동");
			member.setCreatedDate(LocalDateTime.now());
			entityManager.persist(member);

			entityManager.flush();
			entityManager.clear();

			//프록시
			Member findMember = entityManager.getReference(Member.class, member.getId());
			System.out.println("findMember.username = " + findMember.getUserName());

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
