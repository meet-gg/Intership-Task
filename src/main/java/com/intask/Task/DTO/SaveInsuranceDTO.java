package com.intask.Task.DTO;

import com.intask.Task.Entity.PaymentFrequency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveInsuranceDTO {
    private Long id;
    private String name;
    private String description;
    private Double premiumPrice;
    private Double coverageAmount;
    private Integer durationInYears;
    private PaymentFrequency paymentFrequency; // MONTHLY or YEARLY
    private Integer minAge;
    private Integer maxAge;
    private Double minIncome;
    private String gender;
}
