package ex3.inheritance_table_per_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //상속관계 매핑 - 구현 클래스마다 테이블 전략
//@DiscriminatorColumn //이 전략에선 의미가 없음 - 넣어도 사용안됨
public abstract class Item {

    /**
     * 이 전략은 추천하지 않는다
     *
     * 구현 클래스마다 테이블 전략 장점
     * - 서브 타입을 명확하게 구분해서 처리할 때 효과적
     * - not null 제약조건 사용 가능
     *
     * 구현 클래스마다 테이블 전략 단점
     * - 여러 자식 테이블을 함께 조회할 때 성능이 매우 저하됨(UNION 사용)
     * - 자식 테이블을 통합해서 쿼리하기 어려움
     */

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

}
