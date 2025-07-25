package dev.ayushbadoni.MyEcom.repositories;


import dev.ayushbadoni.MyEcom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
