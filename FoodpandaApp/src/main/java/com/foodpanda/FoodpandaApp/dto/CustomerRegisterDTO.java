package com.foodpanda.FoodpandaApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
