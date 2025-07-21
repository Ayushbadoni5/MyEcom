package dev.ayushbadoni.MyEcom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}
