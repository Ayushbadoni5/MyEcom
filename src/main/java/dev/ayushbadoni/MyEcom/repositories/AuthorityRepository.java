package dev.ayushbadoni.MyEcom.repositories;

import dev.ayushbadoni.MyEcom.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    List<Authority> findByRoleCode(String user);
}
