package com.infnet.company.repositories;

import com.infnet.company.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);
    boolean existsByCnpjAndIdNot(String cnpj, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
