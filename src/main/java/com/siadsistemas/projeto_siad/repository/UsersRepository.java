package com.siadsistemas.projeto_siad.repository;

import com.siadsistemas.projeto_siad.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    UserDetails findByLogin(String login);

    @Query("SELECT COALESCE(MAX(e.codigo), 0) FROM Bairro e")
    Integer findMaxCodigo();
}
