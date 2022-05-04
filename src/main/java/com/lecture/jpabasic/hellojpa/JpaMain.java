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

//			team.getMembers().add(member1); //주인이 아닌쪽에도 데이터를 셋팅해줘야 35번 라인에서 값을 가져올 수 있다.

//			entityManager.flush();
//			entityManager.clear();

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
