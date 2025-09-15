package com.infnet.company.repositories;

import com.infnet.company.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findByCustomerId(Long customerId);
    List<ProductOrder> findByProductId(Long productId);
}
