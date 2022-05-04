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

