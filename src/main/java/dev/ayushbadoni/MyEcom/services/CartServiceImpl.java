package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.CartItemResponse;
import dev.ayushbadoni.MyEcom.dto.CartResponse;
import dev.ayushbadoni.MyEcom.entities.Cart;
import dev.ayushbadoni.MyEcom.entities.CartItem;
import dev.ayushbadoni.MyEcom.entities.Product;
import dev.ayushbadoni.MyEcom.entities.User;
import dev.ayushbadoni.MyEcom.repositories.CartItemRepository;
import dev.ayushbadoni.MyEcom.repositories.CartRepository;
import dev.ayushbadoni.MyEcom.repositories.ProductRepository;
import dev.ayushbadoni.MyEcom.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public void addToCart(String email, UUID productId, int quantity) {

        if (quantity<=0){
            quantity=1;
        }
        User user = userDetailRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = (Cart) cartRepository.findByUser(user).orElseGet(()-> {
            Cart newCart =  new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        Optional<CartItem> existingitem = cart.getItems().stream()
                .filter(item-> item.getProduct().getId().equals(productId)).findFirst();
        if (existingitem.isPresent()){
            CartItem item = existingitem.get();
            item.setQuantity(item.getQuantity()+ quantity);

        }else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(quantity);

            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);
    }

    @Override
    public void removeFromCart(String email, UUID productId) {
        User user = userDetailRepository.findByEmail(email).orElseThrow();
        Cart cart = (Cart) cartRepository.findByUser(user).orElseThrow();

        cart.getItems().removeIf(item-> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String email) {
        User user = userDetailRepository.findByEmail(email).orElseThrow();
        Cart cart = (Cart) cartRepository.findByUser(user).orElseThrow();

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCart(String email ) {
        User user = userDetailRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Cart cart = (Cart) cartRepository.findByUser(user).orElseThrow();

        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> {

            CartItemResponse response = new CartItemResponse();
            response.setName(item.getProduct().getName());
            response.setPrice(item.getPrice());
            response.setQuantity(item.getQuantity());
            response.setProductId(item.getProduct().getId());
            return response;
        }).toList();

        BigDecimal totalAmount = BigDecimal.ZERO;
        for(CartItemResponse item : itemResponses){
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        CartResponse cartResponse = new CartResponse();
        cartResponse.setItems(itemResponses);
        cartResponse.setTotalAmount(totalAmount);

        return cartResponse;
    }
}
