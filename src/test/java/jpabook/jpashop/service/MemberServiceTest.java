package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // 스프링 부트를 띄운 상태에서 테스트!, 스프링 컨테이너 안에서 테스트를 돌린다.
@Transactional // 테스트 끝난 후 롤백을 해준다.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Rollback(false)
    @Test
    @DisplayName("회원가입")
    public void 회원가입() throws Exception {
        // given, when, then에 대한 김영한님 설명!
        // 이런게 주어졌을 때~ 이렇게 하면~ 이렇게 된다!!!!!!!!!!!!!!!!!!!!
        // 머리, 가슴, 배

        //given
        Member member = new Member();
        member.setName("kim");

        //when
        // 커밋을 안하고 롤백을 해버린다. 따라서 insert 쿼리가 안나간다!!-> @Rollback(false)를 추가하자!~
        Long savedId = memberService.join(member);

        //then
        // 가능한 이유는 클래스 레벨에서 @Transactional을 설정했기 때문, 같은 트랜잭션 안에서 id 값이 같으면 같은 엔티티를 반환!
        // 같은 영속성 컨텍스트에서 같은 엔티티로 관리된다.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    // 안되네 JUnit4에서는 되는데...
    // @Test(expected = IllegalStateException.class)
    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }

        //then
        fail("예외가 발생해야 하징...");
    }
}