## QueryDSL 소개
- JPQL을 자바 코드로 작성 가능하게 해준다.
- 동적 쿼리 많이 사용
- JPA는 결국 JPQL로 작성한다.
- QueryDsl 하기 위해 Q 객체를 생성해야 한다.
- 의존성 추가!
- where 조건에서 null 리턴 시 사용을 안한다.
- Q Domain은 gitignore에 추가 하자!
- querydsl 사용 시 select 절에 더 쉽고 간결하게 사용할 수 있다.
  - JPQL new 명령어는 비교가 안될 정도로 깔끔한 DTO 조회 지원
- QueryDSL은 JPQL을 코드로 만드는 빌더 역할을 할 뿐이다.
```groovy


```