package jpabook.jpashop;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// JUnit에게 알려준다 스프링 관련 테스트라고~
// @RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    // @Transactional가 있으면 테스트 종료 후 디비를 롤백해버린다.
    @Transactional
    // 그러나 눈으로 보고 싶다면!!! - 테스트 케이스에서 롤백 안하고 커밋해버린다.
    @Rollback(false)
    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // 영속성 컨텍스트 안에서는 같은 id면 같은 Entity~ -> 1차 캐시에서 가져 온다.
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}