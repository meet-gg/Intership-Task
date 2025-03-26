package com.intask.Task.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Double income;
    private String gender;
    private Integer age;
}
