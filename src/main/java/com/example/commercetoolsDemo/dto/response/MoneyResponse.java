package com.example.commercetoolsDemo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// ============= MONEY RESPONSE =============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoneyResponse {
    private String currencyCode;
    private BigDecimal amount;
    private Integer centAmount;
}
