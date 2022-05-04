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

			Member member2 = new Member();
			member2.setUserName("member2");
			member2.setTeam(team);
			entityManager.persist(member2);

			Member member3 = new Member();
			member3.setUserName("member3");
			member3.setTeam(team);
			entityManager.persist(member3);

			entityManager.flush();
			entityManager.clear();

			Member findMember = entityManager.find(Member.class, member3.getId());
			List<Member> members = findMember.getTeam().getMembers();

			for (Member member : members) {
				System.out.println("member = " + member.getUserName());
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
