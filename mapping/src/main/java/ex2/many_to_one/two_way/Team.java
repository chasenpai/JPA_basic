package ex2.many_to_one.two_way;

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
     * 객체 연관관계 = 2개
     * - 회원 > 팀 연관관계 1개(단방향)
     * - 팀 > 회원 연관관계 1개(단방향)
     * - 사실상 서로 다른 단방향이 2개
     *
     * 테이블 연관관계 = 1개
     * - 회원 <> 팀의 연관관계 1개(양방향)
     * - 외래키 하나로 양뱡향 연관계를 가짐
     *
     * mappedBy = 연관관계의 주인
     * - 객체의 두 관계중 하나의 연관관계의 주인으로 지정
     * - 연관관계 주인만이 외래키를 관리
     * - 주인이 아닌쪽은 readOnly
     * - 주인은 mappedBy 속성 사용 X
     * - 주인이 아니면 mappedBy 속성으로 주인 지정
     *
     * 누구를 주인으로 지정해야 하는가?
     * - 비즈니스 로직을 기준으로 주인을 지정하면 안됨
     * - 외래키가 있는 곳을 주인으로 지정
     * - 여기서는 Member.team 이 주인
     * - 진짜 매핑은 연관관계의 주인(Member.team)
     * - 가짜 매핑은 주인의 반대편(Team.members)
     * - 단순하게 N : 1 에서 N인 쪽이 주인이되면 된다
     */
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

}
