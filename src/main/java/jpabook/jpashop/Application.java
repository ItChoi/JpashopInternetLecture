package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    Hibernate5Module hibernate5Module() {
        // LAZY 로딩을 무시하지 않고 JSON 생성 시 LAZY 로딩을 해준다.
        // 이렇게 하면 안된다. 엔티티를 그대로 노출하면 API 스펙 바뀔 시 문제가 된다.
        // 또한 성능상의 문제도 있다. 사용하지 않는 쿼리까지 다 나간다~
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        // 이걸 쓰면 안된다, Hibernate5Module는 필요할 수 있으나 Entity를 외부에 노출하지 말자!
        // hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);

        return hibernate5Module;
    }

}
