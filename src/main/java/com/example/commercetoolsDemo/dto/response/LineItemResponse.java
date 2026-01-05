package com.example.commercetoolsDemo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// ============= LINE ITEM RESPONSE =============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineItemResponse {
    private String id;
    private String productId;
    private String name;
    private Integer quantity;
    private MoneyResponse price;
    private MoneyResponse totalPrice;
    private String productSlug;
    private List<String> imageUrls;
}
