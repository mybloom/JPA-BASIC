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
			Team team = new Team();
			team.setName("TeamA");
			entityManager.persist(team);

			Member member1 = new Member();
			member1.setUserName("member1");
			member1.setTeam(team);
			entityManager.persist(member1);

			entityManager.flush();
			entityManager.clear();

			Team findTeam = entityManager.find(Team.class, team.getId());
			List<Member> members = findTeam.getMembers();

			for (Member member : members) {
				System.out.println("m = " + member.getUserName());
			}

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
