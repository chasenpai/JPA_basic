package ex2.one_to_one.target;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;

    private String name;

    /**
     * 대상 테이블에 외래키
     * - 단방향 관계는 JPA 지원 X
     * - 양방향 관계는 지원
     */
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
