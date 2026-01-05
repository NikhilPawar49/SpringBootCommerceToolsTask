package com.example.commercetoolsDemo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ============= ADDRESS RESPONSE =============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {
    private String id;
    private String streetName;
    private String streetNumber;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
