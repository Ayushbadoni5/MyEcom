package dev.ayushbadoni.MyEcom.auth.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private UUID orderId;
    private String paymentMethod;
    private String message;
    private String razorpayOrderId;
    private String currency;
    private String amount;
}
