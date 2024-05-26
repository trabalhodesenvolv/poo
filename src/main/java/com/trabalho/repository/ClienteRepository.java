package com.trabalho.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trabalho.entity.Cliente;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAll();
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findById(Long id);

}
