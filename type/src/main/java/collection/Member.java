package collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Embedded
    private Address homeAddress;

    /**
     * 값 타입 컬렉션
     * - 값 타입을 하나 이상 저장할 때 사용
     * - @ElementCollection, @CollectionTable 사용
     * - 데이터베이스 컬렉션을 같은 테이블에 저장할 수 없다
     * - 컬렉션을 저장하기 위한 별도의 테이블 필요
     */
    @ElementCollection
    @CollectionTable(
            name = "favorite_food",
            joinColumns = {@JoinColumn(name = "member_id")}
    )
    @Column(name = "food_name")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "address",
            joinColumns = {@JoinColumn(name = "member_id")}
    )
    private List<Address> addressHistory = new ArrayList<>();

}
