package dev.ayushbadoni.MyEcom.controllers;

import dev.ayushbadoni.MyEcom.dto.OrderResponse;
import dev.ayushbadoni.MyEcom.dto.OrderDetails;
import dev.ayushbadoni.MyEcom.dto.OrderRequest;
import dev.ayushbadoni.MyEcom.dto.PaymentStatusUpdateRequest;
import dev.ayushbadoni.MyEcom.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;


    @PostMapping
    public ResponseEntity <?> createOrder(@RequestBody OrderRequest orderRequest, Principal loggedInUser) throws Exception {
        OrderResponse orderResponse = orderService.createOrder(orderRequest,loggedInUser);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @PostMapping("/update-payment-status")
    public ResponseEntity<OrderResponse> updatePaymentStatus(@RequestBody PaymentStatusUpdateRequest request){
        OrderResponse orderResponse = orderService.updatePaymentStatus(request);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderDetails>> getOrderByUser(Principal loggedInUser){
        List<OrderDetails> orders = orderService.getOrderByUser(loggedInUser.getName());
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID id, Principal loggedInUser){
        orderService.cancelOrder(id, loggedInUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
