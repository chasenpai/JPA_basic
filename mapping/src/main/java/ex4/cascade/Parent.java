package ex4.cascade;

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
public class Parent {

    @Id
    @GeneratedValue
    @Column(name = "parent_id")
    private Long id;

    private String name;

    /**
     * 영속성 전이 CASCADE
     * - 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때 사용
     * - 예) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장
     * - 영속성 전이는 연관관께 매핑과는 아무 관련이 없다
     * - 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공하는 것일 뿐
     * - ALL : 모두 적용
     * - PERSIST : 영속
     * - REMOVE : 삭제
     * - MERGE : 병합
     * - REFRESH :REFRESH
     * - DETACH : DETACH
     * - 라이프 사이클이 거의 동일하고 소유자가 하나일 때 써야 한다
     * - 만약 child 가 다른 엔티티랑 연관관계가 있다면 쓰면 안된다
     *
     * orphanRemoval 고아 객체 제거
     * - 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제한다
     * - 참조가 제거된 엔티티는 다른 곳에서 참조하지 않은 고아 객체로 보고 삭제하는 기능이다
     * - 참조하는 곳이 하나일 때 사용해야 한다
     * - 특정 엔티티가 개인 소유할 때 사용해야 한다
     * - @OneToMany, @OneToOne 만 사용 가능
     * - 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화 하면
     * - 부모를 제거할 때 자식도 함께 제거된다. 마치 CascadeType.REMOVE 처럼 동작한다
     *
     * 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있다다     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    public void addChild(Child child) {
        children.add(child);
        child.setParent(this);
    }

}
