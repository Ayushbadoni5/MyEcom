package dev.ayushbadoni.MyEcom.controllers;

import dev.ayushbadoni.MyEcom.dto.AddToCartRequest;
import dev.ayushbadoni.MyEcom.dto.CartResponse;
import dev.ayushbadoni.MyEcom.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Principal loggedInUser){
        cartService.addToCart(loggedInUser.getName(),request.getProductId(),request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable UUID productId, Principal loggedInUser){
        cartService.removeFromCart(loggedInUser.getName(),productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Principal loggedInUser){
        cartService.clearCart(loggedInUser.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Principal loggedInUser){
        CartResponse response = cartService.getCart(loggedInUser.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
