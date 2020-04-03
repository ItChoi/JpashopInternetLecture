## 스프링 데이터 JPA 소개
- 스프링 데이터 JPA는 JPA 사용 시 지루하게 반복되는 코드를 자동화 해준다.
- extends JpaRepository<Member, Long> -> 구현체를 스프링 데이터 JPA가 알아서 만들어서 넣어준다.
  - 스프링 데이터 JPA가 제공하는 구현체를 제공한다
  - List<Member> findByName(String name); -> 스프링 데이터 JPA가 시그니쳐를 보고 select m from Member m where m.name = ?로 짜버린다 알아서.
- 스프링 데이터 JPA는 JpaRepository라는 인터페이스를 제공하는데, 여기에 기본적인 CRUD 기능이 모두 제공된다!
- findByName 처럼 일반화하기 어려운 기능도 메소드 이름으로 정확한 JPQL 쿼리를 실행한다!
  - select m from Member m where m.name = :name
- 결국 스프링 JPA는 JPA를 사용해서 기능을 제공할 뿐이다. 따라서 JPA 자체를 잘 이해하고 활용하는 것이 중요하다.
