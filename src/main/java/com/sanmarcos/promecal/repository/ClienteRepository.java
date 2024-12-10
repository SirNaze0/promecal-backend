package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Metodo para encontrar un cliente por su DNI
    Optional<Cliente> findByDni(String dni);
    boolean existsByDni(String dni);
}
