package com.intask.Task.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "insurances")
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private Double premiumPrice; // Premium amount to be paid
    private Double coverageAmount; // Maximum coverage offered by policy
    private Integer durationInYears; // Duration of the policy in years

//    @UniqueElements()
    @Enumerated(EnumType.STRING)
    private PaymentFrequency paymentFrequency; // Monthly/Yearly premium

    @OneToMany(mappedBy = "insurance", cascade = CascadeType.ALL)
    private List<Purchase> purchases;

    // ✅ Add missing fields for curated insurance logic
    private Integer minAge;
    private Integer maxAge;
    private Double minIncome;
    private String gender; // Male, Female, Any

}

