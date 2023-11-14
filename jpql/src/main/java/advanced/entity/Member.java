package advanced.entity;

import basic.enums.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
//Named 정적 쿼리
@NamedQuery(
        name = "Member.findByName",
        query = "select m from Member m where m.name = :name"
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
