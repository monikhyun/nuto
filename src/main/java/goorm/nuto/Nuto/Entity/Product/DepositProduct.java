package goorm.nuto.Nuto.Entity.Product;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositProduct extends Product {

    private int period;         // 기간
    private double interestRate; // 연이율
    private Long maxAmount;     // 최대 납입 금액
    private String eligibility; // 가입 대상
}