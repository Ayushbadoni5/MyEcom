package dev.ayushbadoni.MyEcom.dto;


import dev.ayushbadoni.MyEcom.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetails {
    private UUID id;
    private Date orderDate;
    private AddressRequest address;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private String shipmentNumber;
    private Date expectedDelivery;
    private List<OrderItemDetails> orderItemList;

}
