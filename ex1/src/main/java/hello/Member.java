package hello;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member {

    @Id
    private Long id;

    private String name;

    public Member() { //JPA 엔티티는 기본생성자가 필수
    }

}
