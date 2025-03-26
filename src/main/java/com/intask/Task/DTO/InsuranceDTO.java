package com.intask.Task.DTO;

import com.intask.Task.Entity.PaymentFrequency;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDTO {
    private Long id;
    private String name;
    private String description;
    private Double premiumPrice;
    private Double coverageAmount;
    private Integer durationInYears;
    private PaymentFrequency paymentFrequency; // MONTHLY or YEARLY
}
