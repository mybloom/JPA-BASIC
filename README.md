# JPA 기본편

인프런 김영한 JPA기본편을 보고 정리한 저장소입니다.


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




