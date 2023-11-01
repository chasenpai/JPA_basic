package ex3.mapped_superclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * @MappedSuperclass
     * - 공통 매핑 정보가 필요할 때 사용
     * - 상속관계 매핑 X
     * - 엔티티 X, 테이블과 매핑 X
     * - 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공한다
     * - BaseEntity 조회, 검색 불가
     * - 직접 생성해서 사용할 일이 없기 때문에 추상 클래스 권장
     * - 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
     * - @Entity 클래스는 엔티티나 @MappedSuperclass 로 지정된 클래스만 상속 가능
     */

    private String createdBy;

    private LocalDateTime createdDate;

    private String lasModifiedBy;

    private LocalDateTime lastModifiedDate;

}
