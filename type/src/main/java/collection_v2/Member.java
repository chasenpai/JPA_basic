package collection_v2;

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

    @ElementCollection
    @CollectionTable(
            name = "favorite_food",
            joinColumns = {@JoinColumn(name = "member_id")}
    )
    @Column(name = "food_name")
    private Set<String> favoriteFoods = new HashSet<>();

    //값 타입 컬렉션 대안으로 일대다 관계를 사용
    //해당 예제에서는 AddressEntity 의 생명주기를 관리하기 위해 일대다 단방향 설정
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<AddressEntity> addressHistory = new ArrayList<>();

}
