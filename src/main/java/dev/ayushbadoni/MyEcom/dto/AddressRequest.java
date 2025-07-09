package dev.ayushbadoni.MyEcom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddressRequest {

    private String name;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
}
