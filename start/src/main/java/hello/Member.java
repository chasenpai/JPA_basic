package hello;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "member")
@Getter
@Setter
//@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR", table = "MY_SEQUENCES", pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {

    @Id
    /**
     * IDENTITY - 데이터베이스에 위임, MYSQL
     * SEQUENCE- 데이터베이스 스퀸스 오브젝트 사용, ORACLE, @SequenceGenerator 필요
     * TABLE - 키 생성용 테이블 사용, 시퀸스를 흉내, 모든 DB, @SequenceGenerator 필요
     * AUTO - 방언에 따라 자동 지정(기본값)
     *
     * 권장하는 식별자 전략
     * - null 이 아니고 유일해야 하며 변하면 안된다
     * - 미래가지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자
     * - 예) 주민등록번호도 변경될 수 있어서 기본키로 적절하지 않다
     * - Long + 대체키 + 키 생성전략 사용
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id; //Long 권장

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
