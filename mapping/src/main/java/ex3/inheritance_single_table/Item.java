package ex3.inheritance_single_table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 매핑 - 단일 테이블 전략
//@DiscriminatorColumn //DTYPE 컬럼 - 단일 테이블 전략은 필수(생략 가능)
public class Item {

    /**
     * 단일 테이블 전략 장점
     * - 조인이 필요 없기 때문에 일반적으로 조회 성능이 빠름
     * - 조회 쿼리가 단순함
     *
     * 단일 테이블 전략 단점
     * - 자식 엔티티가 매핑한 컬럼은 모두 null 허용
     * - 단일 테이블에 모든 것을 저장하기 때문에 테이블이 커질 수 있음
     * - 상황에 따라서 조회 성능이 오히려 느려질 수 있다
     */

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

}
