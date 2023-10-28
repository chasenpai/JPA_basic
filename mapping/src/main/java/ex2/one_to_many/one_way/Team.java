package ex2.one_to_many.one_way;

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
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    /**
     * 일대다 단방향
     * - 1 : N 에서 1이 연관관계의 주인
     * - 테이블 일대다 관계는 항상 N 쪽에 외래키가 있음
     * - 객체와 테이블의 차이 때문에 반대편 테이블의 외래키를 관리하는 특이한 구조
     * - @JoinColumn 을 꼭 사용해야 한다. 그렇지 않으면 조인 테이블 방식을 사용(조인을 하기 위한 중간 테이블 생성)
     */
    @OneToMany
    @JoinColumn(name = "team_id")
    private List<Member> members = new ArrayList<>();

}
