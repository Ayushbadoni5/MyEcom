package dev.ayushbadoni.MyEcom.repositories;

import dev.ayushbadoni.MyEcom.entities.User;
import dev.ayushbadoni.MyEcom.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository <Order, UUID>{
    List<Order> findByUser(User user);

    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

}
