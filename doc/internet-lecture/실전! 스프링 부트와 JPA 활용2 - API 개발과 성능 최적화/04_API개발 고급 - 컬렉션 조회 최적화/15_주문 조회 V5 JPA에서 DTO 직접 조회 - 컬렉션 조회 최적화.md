## 주문 조회 V5 JPA에서 DTO 직접 조회 - 컬렉션 조회 최적화
- orderItem -> Map 바꾸기, key가 orderId가 되고, 값은 OrderItemQueryDto가 된다.
```java
    Map<Long, List<OrderItemQueryDto>> collect = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
    result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
```