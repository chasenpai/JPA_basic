package advanced.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private int amount;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
