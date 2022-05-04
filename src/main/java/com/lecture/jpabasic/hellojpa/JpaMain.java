package com.lecture.jpabasic.hellojpa;

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
			Team team = new Team();
			team.setName("TeamA");
			entityManager.persist(team);

			Member member = new Member();
			member.setUserName("member1");
			member.setTeam(team);
			entityManager.persist(member);

			entityManager.flush();
			entityManager.clear();

			Member findMember = entityManager.find(Member.class, member.getId());
			Team findTeam = findMember.getTeam();
			System.out.println("**findTeam = " + findTeam.getName());

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
