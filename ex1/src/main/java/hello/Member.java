package hello;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member {

    @Id
    private Long id;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING) //enum 이름을 DB 에 저장 - ORDINAL 사용 X
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) //LocalDate, LocalDateTime 사용 시 생략 가능
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob //BLOB, CLOB 타입 매핑
    private String description;

    @Transient //필드 매핑 & 저장 & 조회 X - 주로 메모리상에서 임시로 값 보관 시 사용용
   private Integer temp;

    public Member() { //JPA 엔티티는 기본생성자가 필수 public or protected - 자바 리플렉션을 활용하여 동적으로 객체를 생성
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
