package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryOld {

    // 스프링 부트, 스프링 라이브러리를 사용 시 EntityManager를 @PersistenceContext 대신 @Autowired를 써도 인식 해준다! 그러면 또 @RequiredArgsConstructor를 사용 하여 코드를 줄이고 효율적으로 사용 가능
    // JPA가 제공하는 표준 애노테이션
    // 스프링이 만들어서 제공 받는다.
    private final EntityManager em;

    // 직접 엔티티 매니저 팩토리를 주입 받고 싶다면?
    // 직접 주인받을 수 있다.!
    @PersistenceUnit
    private EntityManagerFactory emf;


    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // JPQL은 SQL과 차이점이 있다.
    // SQL은 테이블을 대상으로 쿼리
    // JPQL은 Entity 객체를 대상으로 쿼리를 날린다.
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        // :name -> 파라미터 바인딩
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", name)
                    .getResultList();
    }

}
