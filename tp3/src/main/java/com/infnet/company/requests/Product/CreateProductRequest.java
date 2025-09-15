package com.infnet.company.requests.Product;

import com.infnet.company.models.Product;
import com.infnet.company.repositories.SupplierRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "O nome do produto é obrigatório")
        @Length(max = 200, message = "O nome deve ter no máximo 200 caracteres.")
        String name,

        @Length(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal price,

        @NotNull(message = "A disponibilidade é obrigatória")
        Boolean available,

        @NotNull(message = "O ID do fornecedor é obrigatório")
        @Positive(message = "O ID do fornecedor deve ser positivo")
        Long supplierId
) {
        public Product createProduct(SupplierRepository supplierRepository) {
                var product = new Product();
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setAvailable(available);
                var supplier = supplierRepository.findById(supplierId)
                        .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
                product.setSupplier(supplier);
                return product;
        }
}