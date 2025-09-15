package com.infnet.company.requests.ProductOrder;

import com.infnet.company.models.ProductOrder;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.repositories.ProductOrderRepository;
import com.infnet.company.repositories.ProductRepository;
import com.infnet.company.repositories.SupplierRepository;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record UpdateProductOrderRequest(

        @Positive(message = "O ID é obrigatório")
        Long id,

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
        public ProductOrder updateProductOrder(CustomerRepository customerRepository,
                                               ProductRepository productRepository,
                                               ProductOrder productOrder) {

                var customer = customerRepository.findById(customerId)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                var product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

                productOrder.setCustomer(customer);
                productOrder.setProduct(product);
                productOrder.setQuantity(quantity);
                productOrder.setOrderDate(orderDate);
                productOrder.setPaid(paid);
                return productOrder;
        }

}
