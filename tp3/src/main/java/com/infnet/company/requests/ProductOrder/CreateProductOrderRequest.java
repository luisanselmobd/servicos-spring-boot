package com.infnet.company.requests.ProductOrder;

import com.infnet.company.models.ProductOrder;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.repositories.ProductRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateProductOrderRequest(
        @NotNull(message = "O ID do cliente é obrigatório")
        @Positive(message = "O ID do cliente deve ser positivo")
        Long customerId,

        @NotNull(message = "O ID do produto é obrigatório")
        @Positive(message = "O ID do produto deve ser positivo")
        Long productId,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser positiva")
        Integer quantity,

        @NotNull(message = "A data do pedido é obrigatória")
        LocalDateTime orderDate,

        @NotNull(message = "O status de pagamento é obrigatório")
        Boolean paid
) {
    public ProductOrder createProductOrder(ProductRepository productRepository, CustomerRepository customerRepository) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        var order = new ProductOrder();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setOrderDate(orderDate);
        order.setPaid(paid);
        return order;
    }



}