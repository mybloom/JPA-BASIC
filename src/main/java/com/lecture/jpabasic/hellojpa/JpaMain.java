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
			member.setTeamId(team.getId()); //외래키 식별자를 직접 다룬다.
			entityManager.persist(member);

			Member findMember = entityManager.find(Member.class, member.getId());

			Long findTeamId = findMember.getTeamId();
			Team findTeam = entityManager.find(Team.class, findTeamId);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			entityManager.close();
		}

		entityManagerFactory.close();
	}
}
