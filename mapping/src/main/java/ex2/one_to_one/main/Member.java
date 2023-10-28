package ex2.one_to_one.main;

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
     * 일대일 관계
     * - 일대일 관계는 그 반대도 일대일
     * - 주 테이블이나 대상 테이블 중에 외래키 선택 가능
     * - 외래키에 데이터베이스 유니크 제약조건 추가
     * - 양방향 매핑시 다대일 처럼 외래키가 있는 곳이 연관관계의 주인
     *
     * 주 테이블에 외래키
     * - 주 객체가 대상 객체의 참조를 가지는 것 처럼 주 테이블에 외래키를 두고 대상 테이블을 찾음
     * - 객체지향 개발자 선호
     * - JPA 매핑 편리
     * - 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
     * - 단점으로는 값이 없으면 외래키에 null 허용
     *
     * 대상 테이블에 외래키
     * - 대상 테이블에 외래키가 존재
     * - 전통적인 데이터베이스 개발자 선호
     * - 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
     * - 단점으로는 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨
     * - JPA 지원을 받으려면 양방향으로 만들어야 함
     */
    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;

}
