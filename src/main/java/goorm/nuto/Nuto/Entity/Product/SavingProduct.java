package goorm.nuto.Nuto.Entity.Product;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingProduct extends Product {

    private int period;
    private double interestRate;
    private Long maxAmount;
    private String eligibility;
}
