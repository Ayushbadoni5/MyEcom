package dev.ayushbadoni.MyEcom.repositories;

import dev.ayushbadoni.MyEcom.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    Authority findByRoleCode(String user);
}
