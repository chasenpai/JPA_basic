package ex1;

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
     * 임베디드 타입
     * - 새로운 값 타입을 직접 정의할 수 있다
     * - 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 한다
     *
     * 임베디드 타입 사용 법
     * - @Embeddable : 값 타입을 정의하는 곳에 표시
     * - @Embedded : 값 타입을 사용하는 곳에 표시
     * - 기본 생성자 필수
     *
     * 임베디드 타입의 장점
     * - 재사용성과 높은 응집도
     * - 해당 값 타입만 사용하는 의미있는 메서드를 만들 수 있음
     * - 임베디드 타입을 포함한 모든 값 타입은 값 타입을 소유한 엔티티에 생명주기를 의존한다
     *
     * 임베디드 타입과 테이블 매핑
     * - 임베디드 타입은 엔티티의 값일 뿐이다
     * - 임베디드 타입을 사용하기 전과 후 매핑하는 테이블은 같다
     * - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
     * - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음음
     * - 임베디드 타입의 값이 null 이면 매핑한 컬럼 값은 모두 null
    */
    //기간
    @Embedded
    private Period workPeriod;

    //주소
    @Embedded
    private Address homeAddress;

    //속성 재정의 - 한 엔티티에서 같은 값 타입을 사용하면 컬럼명이 중복되기 때문에 컬럼명 속성을 재정의
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "work_city")),
            @AttributeOverride(name = "street", column = @Column(name = "work_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "work_zipcode")),
    })
    private Address workAddress;

}
