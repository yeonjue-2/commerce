package hello.commerce.product.model;

import hello.commerce.common.model.BaseEntity;
import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int stock;

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stock -= quantity;
    }
}
