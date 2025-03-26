package com.intask.Task.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {
    private Long userId;
    private Long insuranceId;
}
