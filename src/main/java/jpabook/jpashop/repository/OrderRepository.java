package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 조회 로직 추가로 인해 나중에 개발발
    public List<Order> findAllByString(OrderSearch orderSearch) {
        // JPQAL

        /*
        검색 조건이 있다면????
        em.createQuery("select o from Order o join o.member m"
                + " where o.status = :status"
                + " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
        */
        // (1) 무식한 방법 START -> 실무에서는 안 쓴다. -> 문자를 더하면서 쓰는 것은 안쓴다.
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
        // (1) 무식한 방법 END -> 실무에서는 안 쓴다.
    }

    /**
     * JPA 표준 스펙
     * JPA Criteria -> 권장 방법이 아니다!!
     * 실무에서 쓰라고 만든 것이 아닌듯?!!??
     * 치명적 단점 -> 유지보수성 제로..... 가독성이 별로다
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        // JPA 제공하는 표준 동적 쿼리 -> JPQL을 쿼리로 할 수 있게!
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);

        return query.getResultList();


    }

    // 한방 쿼리로 셀렉트 절에 다 넣고 다 떙겨 온다.
    // member, delivery LAZY를 무시하고, 진짜 객체에 값을 다 채워서 가져온다.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d"
                , Order.class
        ).getResultList();
    }


    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }

    public List<Order> findAll2(OrderSearch orderSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        return query
                .select(order)
                .from(order)
                .where(statusEq(orderSearch.getOrderStatus()))
                .fetch();
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        // 결국 JPQL로 바껴서 쿼리가 나간다.
        return query.select(order)
                .from(order)
                .join(order.member, member)
                // .where(statusEq(orderSearch.getOrderStatus())) // 동적 쿼리
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName())) // 동적 쿼리가 아닌 정적 쿼리
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }

        // return member.name.like(orderSearch.getMemberName());
        return QMember.member.name.like(memberName);
    }


    // null 반환 시 where에서 null은 사용을 안한다.
    private BooleanExpression statusEq(OrderStatus statusCondition) {
        if (statusCondition == null) {
            return null;
        }

        return QOrder.order.status.eq(statusCondition);
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
