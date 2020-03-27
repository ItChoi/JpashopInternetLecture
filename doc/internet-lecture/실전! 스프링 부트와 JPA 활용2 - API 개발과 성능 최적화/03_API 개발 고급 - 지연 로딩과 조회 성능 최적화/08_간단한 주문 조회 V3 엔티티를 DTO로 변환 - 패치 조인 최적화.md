## 간단한 주문 조회 V3 엔티티를 DTO로 변환 - 패치 조인 최적화
- fetch join
  - 한방 쿼리로 셀렉트 절에 다 넣고 다 떙겨 온다.
  - member, delivery LAZY를 무시하고, 진짜 객체에 값을 다 채워서 가져온다.
```java
public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d"
                , Order.class
        ).getResultList();
    }
```

- fetch join을 통해 데이터 여러 개 조회 시 성능에 좋다.
  - 지연 로딩이 일어나지 않는다. 실무에서 정말 자주 사용하는 기법!
  - 마찬가지로 엔티티를 그대로 찍으면 안좋다 최적화 필요!
  
  