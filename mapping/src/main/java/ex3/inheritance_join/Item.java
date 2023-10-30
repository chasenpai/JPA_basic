package ex3.inheritance_join;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) //상속관계 매핑 - 조인 전략
@DiscriminatorColumn //DTYPE 컬럼
public class Item {

    /**
     * 조인 전략 장점
     * - 테이블 정규화
     * - 외래키 참조 무결성 제약조건 활용
     * - 저장공간 호율화
     *
     * 조인 전략 단점
     * - 조회시 조인을 많이 사용 - 성능 저하
     * - 조회 쿼리가 복잡해짐
     * - 데이터 저장시 insert 쿼리 두번 날림
     */

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

}
