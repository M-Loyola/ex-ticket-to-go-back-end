package com.tickettogo.TicketToGoBackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Integer Id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile_number;
}
