package com.intask.Task.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
    private Long id;
    private Long userId;
    private Long insuranceId;
    private String insuranceName;
    private Double premiumPrice;
    private String purchaseDate;
    private String filepath;
}
