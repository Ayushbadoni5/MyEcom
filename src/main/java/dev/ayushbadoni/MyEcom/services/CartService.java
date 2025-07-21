package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.CartResponse;

import java.util.UUID;


public interface CartService {
    void addToCart(String email, UUID productId, int quantity);
    void removeFromCart (String email, UUID productId);
    void clearCart(String email);
    CartResponse getCart(String email);
}
