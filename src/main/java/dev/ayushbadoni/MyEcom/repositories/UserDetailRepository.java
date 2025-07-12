package dev.ayushbadoni.MyEcom.repositories;


import dev.ayushbadoni.MyEcom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDetailRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
