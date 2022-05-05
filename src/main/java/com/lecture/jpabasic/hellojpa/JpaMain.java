package com.lecture.jpabasic.hellojpa;

import java.time.LocalDateTime;
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
			team.setName("TEAMa");
			/* //이걸 꼭 해줘야 한다! 이 부분을 잊어버린다!
			아래 member를 저장할 때 같이 저장될 것 같은데 아닌가보다.
			member.setTeam(team);
			entityManager.persist(member);*/
			entityManager.persist(team); //이걸 꼭 해줘야 한다!


			Member member = new Member();
			member.setUserName("사용자");
			member.setCreatedBy("홍길동");
			member.setCreatedDate(LocalDateTime.now());
			member.setTeam(team);
			entityManager.persist(member);

			entityManager.flush();
			entityManager.clear();

			Member findMember = entityManager.find(Member.class, member.getId());
			System.out.println("findMember = " + findMember.getTeam().getClass());

			System.out.println("===============");
			findMember.getTeam().getName();
			System.out.println("===============");

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
