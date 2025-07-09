package dev.ayushbadoni.MyEcom.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.ayushbadoni.MyEcom.auth.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {

    @Id
    @GeneratedValue
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Address address;

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "payment_mode",nullable = false)
    private String paymentMethod;

    @Column(nullable = true)
    private String shipmentTrackingNumber;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedDeliveryDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<OrderItem> orderItemList;

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;


    @OneToOne(fetch = FetchType.LAZY,mappedBy = "order", cascade = CascadeType.ALL)
//    @ToString.Exclude
    private Payment payment;
}
