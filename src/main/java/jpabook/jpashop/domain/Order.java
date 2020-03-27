package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 지연 로딩을 쓰기때문에 Member member = new ByteBuddyInterceptor();라는 것이 들어간다. 우리눈에 보이진 않지만!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // cascade = CascadeType.ALL -> delivery 객체만 세팅하면 order 저장 시 delivery도 같이 persist 해준다
    // 각자 persist가 아니라, cascade 활용하면 같이 persist 해준다. (persist 전파)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태: ORDER, CANCEL

    //=== 연관관계 편의 메서드===//
    // 양방향 연관 관계
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    // 양방향 시 컨트롤하는 쪽이 들고 있는게 좋다.
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //=== 생성 메서드 ===//
    public static Order createOrder(Member member, Delivery delivery, OrderItem ... orderItems) {
        // 복잡한 비즈니스 로직을 응집해둔다. 변경 시 이 메서드만 수정하면 된다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        // 처음 상태를 오더로 한다
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //=== 비즈니스 로직 ===//
    /**
     * 주문 취소
     */
    public void cancel() {
        // COMP는 배송 완료
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완룐된 상품은  취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            // 주문 하나에 2개의 상품이 있을 수 있다. 2개를 각각 취소해주는 cancel 메소드
            orderItem.cancel();
        }
    }

    //=== 조회 로직===//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        // 람다 활용하면 가독성이 더 좋게 코드를 작성할 수 있다.
        // totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
