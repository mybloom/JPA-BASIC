# JPA 기본편

인프런 김영한 JPA기본편을 보고 정리한 저장소입니다.


---

## CHAP2 : JPA 소개 

> 연관관계
DB는 양쪽관계 참조가 가능하지만 , 객체는 한 방향으로 참조만 가능하다

> 객체 그래프 탐색
- 객체는 자유롭게 객체를 탐색할 수 있어야 한다.
- 처음 실행한 SQL에 따라 탐색범위가 정해지기 때문에 자유로운 객체 그래프 탐색이 불가능하다.
- 모든 객체를 미리 로딩할 수는 없다.
- 객체답게 모델링 할수록 매핑 작업만 늘어난다.

`객체를 자바 컬렉션에 저장하듯이 DB에 저장할 수는 없을까?`

> JPA
- 자바 진영의 ORM(객체 관계 매핑) 기술 표준
- ORM : Object relational Mapping
- JPA 는 애플리케이션과 JDBC 사이에서 동작

> JPA 소개
- EJB 엔티티 빈(자바표준) - 하이버네이트(오픈소스) -> JPA(자바표준)
- JPA는 인터페이스의 모음이다. 
- JPA 표준 명세를 구현한 3가지 구현제
  - 하이버네이트, 등등
  - 인터페이스의 구현체로 하이버네이트 사용.
  - implementation 'org.hibernate:hibernate-entitymanager'
  - 하이버네이트가 jpa인터페이스를 가지고 있다.
  - javax.persistence-api

> 생산성
- 저장 : jpa.persist(member)
- 조회 : Member member = jpa.find(memberId) 
- 수정 : member.setName("변경데이터")
- JPA는 객체에 컬럼에 추가만 하면 된다.

> 패러다임 불일치
- DB와 객체가 불일치되는 부분을 JPA가 자동으로 조회시에는 join을 저장시에는 insert문을 테이블 별로 생성해서 수행한다. 

> JPA 성능
- 1차 캐시와 동일성 보장
  - 동일한 트랜잭션에서는 객체의 동일성을 보장한다.
  - Repeatable Read 보장
- JDBC BATCH SQL 기능을 사용해서 한번에 SQL 전송
  - 커밋하기 전에만 네트워크 전송하면 되므로 한번에 전송
  - JPA는 옵션 하나로  JDBC BATCH SQL 기능 사용 가능
- 지연로딩과 즉시로딩 : 옵션하나로 설정 가능 
  - 지연로딩 : 객체가 실제 사용될 때 로딩
    - 쿼리가 2번 생성됨 
  - 즉시로딩 : JOIN SQL로 한번에 여관된 객체까지 미리 조회
    - 조인되어서 쿼리 1번으로 조회 
  - 항상 조인된 쿼리 내용이 필요하면 즉시로딩을 선택하고, 가끔 필요하면 지연로딩 선택. 
  - [me] 비즈니스 로직을 잘 알고, 그에 맞게 옵션을 설정하면 된다.

`ORM은 객체와 DBMS 모두 잘 알아야 한다 `

---

## chap3 JPA 시작하기 

프로젝트 생성

### persistence.xml
> hibernate.dialect
- 중요한 부분 
- JPA는 특정 데이터베이스 종속적이지 않는다.
- 그래서 SQL 표준 문법이 아닌 DB벤더에 따라 다른 점을 dialect(방언)이라 표현했다.
  - 예) mysql : limit, Oracle: rownum
- 그러므로 아래 설정은, H2 방언(dialect)을 사용했다는 것을 표시한 것이다.
```xml
 <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
```

### javax.persistence 속성
- javax라는 것은 JPA구현체를 하이버네이트를 사용하지 않아도 사용할 수 있는 것이다.
- 표준 옵션

```xml
 <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
 <property name="javax.persistence.jdbc.user" value="sa"/>
 <property name="javax.persistence.jdbc.password" value=""/>
 <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/jpabasic"/>
 <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
```

### 간단하게 JPA 실행해보기 

- 문제 : unknown entity라는 에러로 JPA까지 가기 전에 엔티티로 인식하지 못하고 실행되지 않았다.
- 김영한님 답변 
  - 인프런에 같은 문제로 올린 답변 참고
  - 빌드 환경(gradle빌드)에 따라서 클래스 인식이 자동으로 안되는 경우도 있습니다.    
  따라서 이때는 persistence.xml에 다음과 같이 <class></class>로 엔티티를 추가해주세요.
  - 보통 스프링과 함께 JPA를 사용하게 됩니다. 스프링과 함께 사용하면 `자동으로 엔티티를 스캔`하는 기능이 내장되어 있어서 이런 추가 설정없이 잘 동작합니다.
  지금처럼 순수 JPA를 학습할 때만 이렇게 클래스를 추가하는게 빌드 환경에 따라 필요할 수 있습니다
  ```xml
  <persistence-unit name="hello">
       <class>hellojpa.Member</class>
       <properties>
  ```

> 트랜잭션
- EntityManagerFactory : 애플리케이션에서 1개
- EnitityManager : 쓰레드간 공유하지 않는다. 사용하고 버려야 한다.
- [중요] JPA는 모두 transaction안에서 실행해야 한다.

> JPQL 
- 가장 단순한 조회 방법
- EntityManager.find()
  - select 하는 대상이 테이블이 아니라 객체이다. 그래서 `Member`라고 적어줘야 한다
```java
List<Member> members = entityManager.createQuery("select m from Member as m", Member.class)
				.getResultList();
```
- 어떤 장점이 있을까?
  - 방언에 맞춰서 paging처리 쿼리 생성해준다.
  - 애플리케이션 필요한 데이터만 db에서 불러오려면 결국 검색 조건이 포함된 sql이 필요하다. 
  - 검색조건을 줄 때 db 방언에 맞춰서 쿼리를 생성해주며, 객체 단위를 가져오게 된다.
  - **객체 지향 sql이다.**

---

## chap4 영속성 관리  - 내부 동작 방식

> JPA에서 가장 중요한 2가지
1. 객체와 관계형 데이터베이스 매핑하기
2. 영속성 컨텍스트

### 영속성 컨텍스트
엔티티 매니저 팩토리와 엔티티 매니저 : 고객 요청에 따라 엔티티매니저 생성한다.

- 의미 : 엔티티를 영구 저장하는 환경
- 영속성 컨텍스트 : 논리적 개념
- 엔티티 매니저 : 영속성 컨텍스트에 접근

- 엔티티매니저 -> 영속성 컨텍스트 (1:1 관계로 생성)

> 엔티티의 생명주기
- 비영속 : new/transient
  - 객체 생성만 상태 
  `Member member = new Member(1L, "memberA");`
- 영속 
  - `entityManager.persist(member);` 한 상태
- 준영속, 삭제
  - `entityManager.detach(member);` 한 상태

> 영속성 컨텍스트의 이점
- 1차 캐시 : 트랜잭션 안에서의 캐시 
- 성능적 이점보다는 컨셉적인 이점이 있다.

> 엔티티 등록
- 트랜잭션을 지원하는 쓰기 지연
- 커밋하는 순간 DB에 INSERT SQL을 보낸다.

> 엔티티 수정 : 변경 감지
- Dirty Check
- 1차 캐시 : id와 entity, 스냅샷이 있다.
- 영속성컨텍스트에서 db로 flush()할 때 엔티티와 스냅샷을 비교한다.
- 1차 캐시 들어온 상태를 스냅샷이라고 한다.
- 스냅샷과 엔티티의 속성이 다를 때 변경 감지하고 update query를 날린다.

> 플러시 발생
- 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영
- 트랜잭션 작동하면 플러시가 자동 발생한다.
- flush()해도 영속성 컨텍스트에서 사라지는 것이 아니다. db에 반영만 된다.

1. entityManager.flush() 호출
2. commit() 시 자동 호출
3. JPQL 실행시 자동 호출

> 플러시 옵션
- 대부분 Auto로 놓고 사용 : 커밋이나 쿼리를 실행할 때 플러시
- `FlushModeType.AUTO`

### 준영속 상태

- em.detach(entity);
- em.clear();

---

## chap5 엔티티 매핑

- 객체와 테이블 매핑
- db스키마 자동 생성
- 필드와 컬럼 매핑
- 기본 키 매핑
- 연관관계 매핑 : @ManyToOne , @JoinColumn

### 객체와 테이블 매핑
 - @Entity(name = "Member") //다른 패키지에 같은 객체가 있을 때나 사용


> 데이터베이스 스키마 자동 생성
- ddl을 애플리케이션 실행 시점에 자동 생성
- 테이블 중심 -> 객체 중심
- 이렇게 생성된 ddl은 개발 장비에서만 사용
- 생성된 ddl은 운영서버에서는 사용하지 않거나, 적절히 다듬은 후 사용

> 데이터베이스 스키마 자동생성 옵션

- 실행 시점에 새로 테이블 만든다
```xml
<property name="hibernate.hbm2ddl.auto" value="create" />
```
- create : drop & create
- create-drop : create 종료시점에 drop
- update : 변경분만 반영 (alter table )
- validate : 객체에 새로운 속성 추가 시, 엔티티와 테이블이 정상 매핑되었는지 확인.
- none : 사용하지 않음.?

> [주의] 데이터베이스 스키마 자동생성 주의
- 운영장비에선 절대 create, create-drop, update 사용하면 안된다!
- 테스트 서버는 update 또는 validate 만 사용
- 개발 초기 단계는 create, update 유용 : 시스템이 자동으로 alter시키는 것은 위험하다. db가 lock 발생.
- local 에서만 자유롭게 사용하고, staging,product에서는 사용하지 않음.

> DDL 생성기능
- table 생성 후 alter table로 제약 조건이 생성됨. 
```java
  @Column(unique = true, length = 10)
  private String name;
```

### 필드와 컬럼 매핑

- @Transient : db 컬럼으로는 생성되지 않고 메모리에서만 사용하는 속성

> @Column
- nullable : not null
- insertable, update = false : 등록/수정이 되지 않게 한다.
- unique는 잘안쓴다. alter table시 constraint 이름이 이상하게 잡혀서.
  - 대신, @Table(uniqueConstraint = ) 로 이름지정해서 사용한다.
- 자바 ENUM 매핑할 때 `@Enumerated(EnumType.STRING)`를 꼭 사용해야 한다. 그러지 않으면 속성 추가 할 때 데이터 꼬인다.

> @Temporal
- LocalData, LocalDataTime을 사용할 때는 생략 가능 : 최신 하이버네이트 지원
- Date등의 옛날 컬럼 사용할 때만 해당 애노테이션 사용

> @Lob
- clob
- blob

### 기본키 매핑

- @Id : 직접 할당
- @GeneratedValue : 자동 생성
  - strategy = GenerationType.IDENTITY : 기본키 생성을 db에 위임하는 것
  - SEQUENCE : 하이버네이트 시퀀스 전략

### IDENTITY 전략
- DB에 들어가야 PK인 ID값이 생기는데 , 영속성 컨텍스트는 무조건 PK값이 있어야 한다. 
- 그러면 아직 DB에 들어가지 않은 영속성 컨텍스트에서는 어떻게 해야할까?
- IDENTITY 전략에서만 `예외적으로 persist()호출 시점`에 db에 `insert 쿼리`를 날린다.
- persiont() 때 DB에 쿼리를 날린다고해서, 성능에 큰 차이는 없다

### SEQUENCE 전략
- allocationSize = 50 : 기본은 50으로 되어 있다.
- 메모리에서는 50개 쌓여도 된다.
- `call next value for MEMBER_SEQ` 를 50개 쌓일 때까지 안해도 된다.
- 여러 웹서버가 있어도 동시성 문제 없이 작동된다.

---

## chap6 연관관계 매핑 기초 

- 객체와 테이블 연관관계 차이를 이해
- 객체의 참조와 테이블의 외래키 매핑
- 단방향, 양뱡향
- 다중성 : 다대일, 일대다, 일대일, 다대다
- 연관관계 주인 

> 연관관계 필요한 이유
- 객체지향 설계의 목표는 자율적인 객체들의 협력 공동체 만드는 것

> 실습 
- 객체를 테이블에 맞춰 모델링 : 외래키 식별자를 직접 다룬다.
- 객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.
```java
            Team team = new Team();
			team.setName("TeamA");
			entityManager.persist(team);

			Member member = new Member();
			member.setUserName("member1");
			member.setTeamId(team.getId()); //외래키 식별자를 직접 다룬다.
			entityManager.persist(member);
```

### 단방향 연관관계

- Member에 Team을 연관관계 맺는다. 
- @ManyToOne, @JoinColum을 지정해준다.
```java
	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;
```

### [중요] 양방향 연관관계의 주인

> 객체와 테이블이 관계를 맺는 차이
- 객체의 양방향은, 단방향 연관관계가 2개 있는 것이다.
- 테이블은 외래키 하나로 양쪽으로 방향을 가질 수 있다.
```sql
select m.*, t.*
from member m
join team t 
on m.team_id = t.team_id;

select * 
from team t
join member m
on t.team_id = m.team_id;
```

> 객체의 양방향, 단방향이 2개 
- 위의 예시로 team_id가 변경되었을 때, `Team team`과 `List Member` 중 어떤 것을 수정해야 할까?
  - Member: id, `Team team`, username
  - Team : id, name, `List Member`
- 그래서 연관관계의 주인을 정해야 한다.
- 연관관계의 주인만이 외래키를 관리할 수 있다. 관리한다는 것은 등록/수정 할 수 있다는 것이다.
- 주인이 아닌 쪽은 `읽기만` 가능하다!
  - Team객체를 등록/수정할 때 Member객체가 변경되면 안된다.
- **주인이 아닌 것을 지정해주는 것이 `mappedBy`**
  ```java
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
  ```
- **외래키가 있는 곳을 주인으로 정한다!**
  - 객체간 방향에서 주인이, 비지니스적으로 주인은 아니다. 

> 양뱡향 매핑시 가장 많이 하는 실수 
- 연관관계 주인에 값을 입력하지 않음
- 양방향 매핑시 무한 루프를 조심하자

> 양방향 연관관계 매핑시, 양쪽 모두 값을 셋팅해줘야 한다.
- 왜냐면, 1차캐싱에서 조회에 오기 때문이다.
- 순수 객체 상태 고려!
- 코드 작성 시 잊어버릴 수 있으므로 편의 메서드를 생성한다. 
  - Member에서 setTeam() 할 때, `team.getMembers().add(this);` 를 해준다.
```java
try {
			Team team = new Team();
			team.setName("TeamA");
			entityManager.persist(team);

			Member member1 = new Member();
			member1.setUserName("member1");
			member1.setTeam(team);
			entityManager.persist(member1);

			team.getMembers().add(member1); //주인이 아닌쪽에도 데이터를 셋팅해줘야 35번 라인에서 값을 가져올 수 있다.
			
            // 이 작업을 해주면 db에 저장되므로 entityManager.find() 시 db에서 값을 가져오지만, 
            // 해당 작업을 하지 않으면, 1차 캐시에서 데이터를 가져오므로 주인이 아닌쪽에도 데이터를 셋팅해줘야 한다.
            // 특히 테스트코드 작성 시, 해당 부분은 유의 해야 한다.
//			entityManager.flush();
//			entityManager.clear();

			Team findTeam = entityManager.find(Team.class, team.getId());
			List<Member> members = findTeam.getMembers();

			for (Member member : members) {
				System.out.println("m = " + member.getUserName());
			}

```

> 양방향 매핑시 무한 루프 조심
- toString(), lombok, `JSON 생성 라이브러리`
  - lombok : toString() 쓰는 것 조심
  - JSON 생성 라이브러리 : 컨트롤러에는 엔티티를 절대 반환하지 않는다. dto로 바꿔서 반환한다!
    - 무한루프
    - 엔티티도 변경될 수 있는데 api 스펙이 바뀐다. : dto로 바꿔서 반환한다!

> 양방향 매핑 정리
- 단방향 매핑만으로도 이미 연관관계 매핑은 완료
- 처음엔 단방향 매핑으로 설계를 끝낸다!
- 단방향 매핑을 잘하고 양방향 필요할 때 추가해도 된다.
- JPQL에서 역방향으로 탐색할 일이 많음

> 연관관계 주인을 정하는 기준
- 비지니스 로직을 기준으로 연관관계 주인을 선택하면 안된다.
- 외래키의 위치를 기준으로 연관관계 주인을 정해야 한다.

---

## chap7 다양한 연관관계 매핑

> 단방향, 양방향
- 객체
  - 참조용 필드가 있는 쪽으로만 참조 가능 

> 다대일 N:1
- 다대일 단방향
- 다대일 양방향 
  - 주인이 아닌쪽에 List<객체명> = new ArrayList<>();
  - @OneToMany(mappedBy = "")

> 일대다 1:N
- 실무에서 이 모델은 거의 지원하지 않는다. 
- Team 엔티티를 수정했는데 Member 테이블이 변경되므로 사용하지 않는다. 
- 이렇게 사용하려면 `다대일 양방향`으로 관계를 맺도록 한다.
```java
	@OneToMany
	@JoinColumn(name = "TEAM_ID") //이거 꼭 사용해야 함. 그렇지 않으면 조인 테이블(중간테이블) 방식 사용
	private List<Member> members = new ArrayList<>();
```

- member의 update 쿼리가 발생한다.
```java
Member member1 = new Member();
			member1.setUserName("member1");
			entityManager.persist(member1);

			Team team = new Team();
			team.setName("TeamA");
			team.getMembers().add(member1);

			entityManager.persist(team); //member의 update 쿼리를 발생시킴

			transaction.commit();
```
- 일대다 양방향도 공식지원은 아니지만 할수는 있다. (굳이 사용할 필요 없음)

> 일대일 
- 일대일 관계는 반대도 일대일
- 주테이블, 대상 테이블 중에 외래키 선택 가능
- 외래키에 db 유니크 제약 조건 추가

- 주 테이블에 외래키 
  - 객체지향 개발자 선호
  - 장점 : 주 테이블만 조회해도 대상 테이블에 데이터 있는지 확인 가능
  - 단점 : 값이 없으면 외래 키에 null 허용해야 한다.
  - 김영한님은 이 방식 선호. 하지만 dba와 협의가 되어야 한다.
- 대상 테이블에 외래키
  - 데이터베이스 개발자 선호
  - 장점 : 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
  - 단점 : 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩된다. : 치명적인데 해결법이 있긴하다
  
> 다대다
- 실무에서 안씀
- 연결테이블을 추가해서 일대다 , 다대일 관계로 풀어내야 한다.
- 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능
- 편리해 보이지만 실무에서 사용안한다.
  - 연결 테이블이 단순히 연결만 하고 끝나지 않는다.
  - 중간테이블에 추가 정보를 넣어야 하는데 그렇게 하지 못한다. 중간테이블이 숨겨져 있게 된다.
- 중간테이블을 엔티티로 만든다. 
  - 그리고 OneToMany, ManyToOne으로 관계를 만든다.
- 중간테이블 김영한님 경험
  - 2개의 연결테이블의 PK를 조합한 pk를 만들지 않고, 해당 키들을 fk로 두고 (혹시 더 필요하면 제약조건을 두는 식으로 해결)
  - 새로운 id를 pk로 만들어 사용.
  - jpa매핑도 좋고, 애플리케이션 개발에 더 유용했다. 

---

## chap8 고급매핑

- 상속관계 매핑
- MappedSuperclass

### 상속관계 매핑

> DB 전략
1. 조인전략 : 정규화 된 방식
2. 단일 테이블 전략 : 성능 때문에 이렇게 하기도 한다. 객체는 상속관계로 되어 있기도 하다
3. 구현 클래스마다 테이블 전략 : 중복이 발생해도 묶이는 테이블 없이 각각을 테이블로 생성

- DB가 어떤 전략이던 JPA는 다 매핑할 수 있도록 지원한다.
- DB 설계가 변경되어도 소스 변경이 되지 않는다.

> JOINED : 조인테이블 형태로 매핑
- @Inheritance(strategy = InheritanceType.JOINED)
- @DiscriminatorColumn(name = "DIS_TYPE")
  - 기본 DTYPE : 어느 테이블에서 들어온 것인지 알기 위해 DiscriminatorColumn를 해준다.
  - name 옵션  : 자동으로는 DTYPE으로 들어왔는데 컬럼명을 명시해 줄 수 있다.
  - 조회시 inner join으로 데이터 가져오는 것 확인
  ```sql
      select
          movie0_.id as id1_2_0_,
          movie0_1_.name as name2_2_0_,
          movie0_1_.price as price3_2_0_,
          movie0_.actor as actor1_3_0_,
          movie0_.director as director2_3_0_ 
      from
          Movie movie0_ 
      inner join
          Item movie0_1_ 
              on movie0_.id=movie0_1_.id 
      where
          movie0_.id=?
     ```

> 단일 테이블 전략
- 기본은 단일 테이블 전략 
- @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
- 옵션을 주지 않았을 때 ITEM 단일 테이블로 생긴다.
- 조인테이블로 갔다가 성능테스트시 성능이 안나올경우, 단일 테이블 전략을 선택하기도 한다.
```sql
    create table Item (
       DTYPE varchar(31) not null,
        id bigint not null,
        name varchar(255),
        price integer not null,
        artist varchar(255),
        author varchar(255),
        isbn varchar(255),
        actor varchar(255),
        director varchar(255),
        primary key (id)
    )
```

> 구현 클래스마다 테이블 전략
- 각 테이블을 묶는 ITEM 테이블이 없다. 그러므로 각 Album, Movie, Book 에 Item 클래스의 속성들이 중복 컬럼으로 들어간다.
- @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
- 데이터 넣을 때는 깔끔하게 한 테이블에 넣지만
- 단점 : 조회시에는 union 으로 3개의 테이블 모두 조인해야 한다.
```sql
    select
        item0_.id as id1_2_0_,
        item0_.name as name2_2_0_,
        item0_.price as price3_2_0_,
        item0_.artist as artist1_0_0_,
        item0_.author as author1_1_0_,
        item0_.isbn as isbn2_1_0_,
        item0_.actor as actor1_3_0_,
        item0_.director as director2_3_0_,
        item0_.clazz_ as clazz_0_ 
    from
        ( select
            id,
            name,
            price,
            artist,
            null as author,
            null as isbn,
            null as actor,
            null as director,
            1 as clazz_ 
        from
            Album 
        union
        all select
            id,
            name,
            price,
            null as artist,
            author,
            isbn,
            null as actor,
            null as director,
            2 as clazz_ 
        from
            Book 
        union
        all select
            id,
            name,
            price,
            null as artist,
            null as author,
            null as isbn,
            actor,
            director,
            3 as clazz_ 
        from
            Movie 
    ) item0_ 
where
    item0_.id=?
```

> 각 전략 장단점
- 조인 전략 
  - item 하나만 조회할 수 있어서 좋다. 특히 order등의 다른 도메인에서 참조 시 편리하다  
  - 정규화 되어 있어서 저장공간 효율화
  - 단점 : 조회할때 조인을 많이 사용하고(잘 사용하면 성능저하 발생 안한다), 쿼리가 복잡하다.
- 단일 테이블 전략
  - 조인이 필요없어서 조회 성능 빠르다.
  - 조회 쿼리 단순
  - 단점 : 자식 엔티티가 매핑한 컬럼은 모두 null 허용해야 한다. : 데이터 무결성 지킬 수 없다.
- 구현 클래스마다 테이블 전략
  - 쓰면 안되는 전략!
  - ORM 전문가도 추천하지 않은 전략
  - 단점 : 여러 자식 테이블 함께 조회할 때 UNION ALL 사용

> 상속관계 매핑 정리
- 관계형 DB에는 상속관계가 없다. 
- 김영한님은 보통은 조인테이블 전략 사용하고, 비즈니스적으로 단순할 경우 단일 테이블 전략을 사용했다.

### @MappedSuperclass

- 공통 매핑 정보가 필요할 때 사용
  - 보통 생성일, 수정일, 생성자, 수정자 등
  - 추후, JPA 이벤트 기능을 사용해서 자동으로 넣어줄 수 있다.
- DB는 완전히 다른데 객체 입장에서 속성만 상속받아 사용하고 싶은 경우. 

```java
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

	private String createdBy;
	private LocalDateTime createdDate;
	private String lastModifiedBy;
	private LocalDateTime lastModifiedDate;

}
```

> 실전예제4
- 실전에서 상속관계를 써서 얻을 수 있는 이점과 김영한님의 경험
- 데이터 많아져도 고민이 되기 때문에 
- 상속관계를 쓸 때도 있고, 
- 애플리케이션이 커지면 복잡해질 때 사용자 많아질 때 테이블 단순하게 유지한다.
  - json으로 말아 넣자고 하는 경우도 있다. : album, book, movie같은 객체를

---

## chap9 프록시와 연관관계 정리

### 프록시

> 프록시
- Member를 조회할 때 Team도 조회해야 할까?
- em.find() vs em.getReference()
  - em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
- 프록시 객체 메커니즘은 JPA 표준이 아니라 구현체마다 각기 다른 방식으로 되어 있다. 
  - 지금 예제에서는 하이버네이트를 알아본 것 

> 프록시 특징
- 하이버네이트 내부적으로 실제 엔티티를 상속받아서 생성된다. 
  - 즉, 타입체크 시 주의해야 한다. 
  - == 비교 실패, instanceof 사용한다!
  - 즉, `refMember instanceof Member`
- 실제 객체의 참조(target)를 멤버 변수로 갖고 있다.
- 영속성 컨텍스트에 엔티티가 이미 있으면 getReference() 호출해도 엔티티가 반환된다.
  - 엔티티와 프록시 == 비교 true


- **getName() 했을 때 프로세스**
  - 영속성 컨텍스트에 `초기화` 요청한다. : `초기화는 1번만 수행`  
    - 초기화 : 실제 엔티티 객체를 영속성 컨텍스트에 db조회해서 생성한다.
    - 초기화1번만 수행한다는 것을 기억해야 한다 : 초기화 후 clear() 시 다시 초기화하지 못한다.    
  - 영속성 컨텍스트의 엔티티에서 getName()을 가져온다.
```java
Member member  = em.getReference(Member.class , "id1");
member.getName(); //target에 있는 엔티티를 직접 조회한다.
```

> 프록시 확인
- 프록시 인스턴스의 초기화 여부 확인
  - PersistenceUnitUtil.isLoaded(Object entity)
- 프록시 강제 초기화
  - JPA표준에서는 없고, 하이버네이트에서 지원
  - Hibernate.initialize(entity);
- 프록시 클래스 확인 방법
  - entity.getClass().getName() 출력

### 즉시로딩과 지연로딩

> 지연로딩
- Member를 조회할 때 Team도 조회해야 할까?
- Team은 실제 사용 시점에 조회할 수 있도록 `지연로딩`으로 셋팅한다.
- member.getTeam().getName() : getName()할 때 로딩된다.
- 2번에 나눠서 로딩되므로 쿼리가 2개로 나뉜다.
```java
public class Member {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Team team;
}
```

> 즉시로딩
- Member 와 Team을 항상 같이 조회한다면?
- @ManyToOne(fetch = FetchType.EAGER)
- 조회시 쿼리를 조인해서 1번에 가져온다.

> 실무
- 가급적 `지연로딩`만 사용 한다!
- 즉시로딩을 적용하면 예상하지 못한 sql이 발생
- 예제처럼 2개 테이블이 아니라 많은 테이블이 조인될 수 있고, 조인 자체의 문제라기 보다는 그 안에서 lock등의 발생으로 문제발생 소지가 있다.

> 즉시로딩 주의점
- JPQL에서 `N+1`의 문제를 일으킨다.
- 즉시로딩으로 설정되어 있어서 JPQL의 SQL 이외의 Team 관련 쿼리가 발생된다.
  - 심지어 Member의 수만큼 Team 조회 쿼리가 발생한다.
  - LAZY로 세팅할 경우는 발생하지 않음.
- [주의] @ManyToOne, @OneToOne은 `기본 즉시 로딩`으로 설정 되어 있다. - `LAZY로 변경설정` 해야 한다.


> JPQL에서 `N+1`의 문제 해결법
- 모든 연관관계를 지연로딩으로 세팅 한 후 아래 3가지 방법이 있다.
1. JPQL의 fetch join 사용 - 동적으로 원하는 쿼리 사용. case by case로 사용하기 위해 
2. 엔티티그래프? 라는 애노테이션 사용
3. 배치 사이즈라고 하는 방법 - 쿼리가 1+1 

### 영속성 전이 CASCADE 

- 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장
- `entityManager.persist(parent)` 할 때 , child도 persist 되었으면 할 때 사용
- parent가 child를 관리해줬으면 할 때 사용 
- 연관관계 매핑하는 것과 아무 상관 없음

```java
entityManager.persist(parent); // child도 persist 되었으면 할 때 사용
entityManager.persist(child1);
entityManager.persist(child2);
```

- `cascade = CascadeType.ALL` 사용.
- 옵션은 ALL, PERSIST(저장할 때만) 정도 사용
```
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
private List<Child> childList = new ArrayList<>();
```

### 고아 객체

- Parent 컬렉션에서 빠진 Child는 db에서 delete된다.
- 참조하는 곳이 하나일 때 사용해야 한다.
- 부모 제거하면 자식은 고아가 되기 때문에 객체제거기능 활성화하면, 부모 제거할 때 자식도 함께 삭제 된다. 
- CascadeType.REMOVE 처럼 동작
```java
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Child> childList = new ArrayList<>();
```

