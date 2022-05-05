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
			Team team = new Team();
			team.setName("TEAMa");
			/* //이걸 꼭 해줘야 한다! 이 부분을 잊어버린다!
			아래 member를 저장할 때 같이 저장될 것 같은데 아닌가보다.
			member.setTeam(team);
			entityManager.persist(member);*/
			entityManager.persist(team); //이걸 꼭 해줘야 한다!

			Team team2 = new Team();
			team2.setName("TEAMb");
			entityManager.persist(team2);


			Member member = new Member();
			member.setUserName("사용자");
			member.setCreatedBy("홍길동");
			member.setCreatedDate(LocalDateTime.now());
			member.setTeam(team);
			entityManager.persist(member);

			Member member2 = new Member();
			member2.setUserName("사용자2");
			member2.setCreatedBy("홍길동2");
			member2.setCreatedDate(LocalDateTime.now());
			member2.setTeam(team2);
			entityManager.persist(member2);

			entityManager.flush();
			entityManager.clear();

			List<Member> members = entityManager.createQuery("select m from Member m join fetch m.team", Member.class)
									.getResultList();

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
