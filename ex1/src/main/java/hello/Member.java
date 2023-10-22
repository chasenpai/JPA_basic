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

    public Member() { //JPA 엔티티는 기본생성자가 필수 public or protected - 자바 리플렉션을 활용하여 동적으로 객체를 생성
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
