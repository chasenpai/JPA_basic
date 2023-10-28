package ex2.many_to_many;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String name;

    /**
     * 다대다 매핑
     * - 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다
     * - 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야 한다
     * - 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능
     *
     * 다대다 매핑의 한계
     * - 편리해 보이지만 실무에서 사용 X
     * - 연결 테이블이 단순히 연결만 하고 끝나지 않는다
     * - 주문시간, 수량 같은 데이터가 들어올 수 있음
     * - 중간 테이블에 추가 정보를 더 넣는 것이 불가능하다
     *
     * 다대다 한계 극복
     * - 연결 테이블용 엔티티를 추가
     * - @ManyToMany > @OneToMany, @ManyToOne
     * - 그냥 쓰지말자
     */
    @ManyToMany
    @JoinTable(name = "member_product") //연결 테이블 지정
    private List<Product> products = new ArrayList<>();

}
