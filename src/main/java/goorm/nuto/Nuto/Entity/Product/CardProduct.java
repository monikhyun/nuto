package goorm.nuto.Nuto.Entity.Product;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardProduct extends Product {

    private String cashback;
    private String point;
    private String annualFee;
}
