package ex2.v2;

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

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    //연관관계 편의 메서드
    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
