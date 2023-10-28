package ex2.one_to_many.two_way;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
     * 일대다 양방향
     * - 이런 매핑은 공식적으로 존재하지 않음
     * - insertable = false, updatable = false 속성으로 읽기 전용 필드로 만든다
     * - 그냥 다대일 양방향을 사용하자
     */
    @ManyToOne
    @JoinColumn(name = "team_id", insertable = false, updatable = false) //읽기 전용
    private Team team;

}
