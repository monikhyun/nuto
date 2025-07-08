package goorm.nuto.Nuto.Entity.Product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // TABLE_PER_SUBCLASS 구조
@DiscriminatorColumn(name = "product_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // 상품명
    private String bank;      // 은행
    private String benefit;   // 혜택
    private String url;       // 링크
}