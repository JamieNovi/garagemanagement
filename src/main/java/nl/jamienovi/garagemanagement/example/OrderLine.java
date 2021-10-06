package nl.jamienovi.garagemanagement.example;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ORDER_LINE")
@Getter
@Setter
@NoArgsConstructor
public class OrderLine {
    @Id
    @SequenceGenerator(name = "ORDER_LINE_SEQ", sequenceName = "ORDER_LINE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ORDER_LINE_SEQ", strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ORDER_ID", nullable = false, updatable = false)
    private Order parent;

//    @ManyToOne
//    @JoinColumn(name = "PRODUCT_ID", nullable = false)
//    private Product product;

    @Column(name = "QUANTITY")
    private BigDecimal quantity;

    @Column(name = "PRICE")
    private BigDecimal price;
}