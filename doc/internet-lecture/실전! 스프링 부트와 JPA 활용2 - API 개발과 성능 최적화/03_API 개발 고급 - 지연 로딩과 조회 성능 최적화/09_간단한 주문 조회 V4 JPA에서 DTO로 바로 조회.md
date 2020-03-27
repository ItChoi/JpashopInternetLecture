## 간단한 주문 조회 V4 JPA에서 DTO로 바로 조회
- 엔티티 조회 후 DTO 변환이 필요 없이 JPA에서 바로 DTO로 끄집어 내는 것!!!
```java

public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

public List<OrderSimpleQueryDto> findOrderDtos() {
        // o를 엔티티로 넘기면 안된다 엔티티가 아닌 식별자로 넘어간다.
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d",
                OrderSimpleQueryDto.class
        ).getResultList();
    }
```

- select 절이 페치 조인 보다 준다. 직접 쿼리를 짰기에, fetch join은 기본적으로 조인 성능은 같으나 셀렉트 절에서 많이 db에서 가져 온다 따라서 네트웍을 더 쓰긴 쓴다.
- v3, v4는 트레이드 오프가 있기에 뭐가 더 좋은지 우열을 가리기 힘들다.
- v3이 재사용성이 조금 더 좋다. 위에 코드가 v4!
- 리파지토리는 엔티티를 다루기에 v3 까진 괜찮다. 성능 최적화용 쿼리 용도로 기파지토리 폴더에 하나 더 판다.
- 쿼리 방식 선택 권장 순서
  - 엔티티를 DTO로 변환 ! -> 유지보수성이 좋다.
  - 필요하면 페치 조인으로 성능 최적화! 대부분 성능 이슈 해결!
  - 그래도 안될 때 DTO로 직접 조회하는 방법!
  - 최후의 방법으로 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template 사용하여 SQL을 직접 사용!