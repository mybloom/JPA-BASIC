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
  



  

