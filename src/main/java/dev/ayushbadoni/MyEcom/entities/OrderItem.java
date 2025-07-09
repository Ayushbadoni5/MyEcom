package dev.ayushbadoni.MyEcom.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "order_items")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    private UUID productVariantId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @Column(nullable = false)
    private Integer quantity;


    private Double itemPrice;
}
