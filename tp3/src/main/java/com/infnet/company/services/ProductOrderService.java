package com.infnet.company.services;

import com.infnet.company.models.ProductOrder;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.repositories.ProductOrderRepository;
import com.infnet.company.repositories.ProductRepository;
import com.infnet.company.requests.ProductOrder.CreateProductOrderRequest;
import com.infnet.company.requests.ProductOrder.UpdateProductOrderRequest;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductOrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    public ProductOrderService(CustomerRepository customerRepository,
                               ProductRepository productRepository,
                               ProductOrderRepository productOrderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
    }
    @Transactional
    public List<ProductOrder> getAllOrders() {
        var productsOrders = productOrderRepository.findAll();
        for(ProductOrder productOrder : productsOrders) {
            Hibernate.initialize(productOrder.getCustomer());
            Hibernate.initialize(productOrder.getProduct());
        }
        return productsOrders;
    }
    @Transactional
    public ProductOrder getOrderById(Long id) {
        var productOrder = productOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido inexistente"));
        Hibernate.initialize(productOrder.getCustomer());
        Hibernate.initialize(productOrder.getProduct());
        return productOrder;
    }

    public List<ProductOrder> getOrderByCustomerId(Long id) {
        return productOrderRepository.findByCustomerId(id);
    }

    public void deleteOrder(Long id) {
        ProductOrder order = getOrderById(id);
        productOrderRepository.delete(order);
    }

    public ProductOrder createOrder(CreateProductOrderRequest request) {
        if (!customerRepository.existsById(request.customerId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente inexistente");
        if (!productRepository.existsById(request.productId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto inexistente");

        return productOrderRepository.save(request.createProductOrder(productRepository, customerRepository));
    }

    public void updateOrder(UpdateProductOrderRequest request) {
        var productOrder = productOrderRepository.findById(request.id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido inexistente"));
        if (!customerRepository.existsById(request.customerId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente inexistente");
        if (!productRepository.existsById(request.productId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto inexistente");
        productOrderRepository.save(request.updateProductOrder(customerRepository, productRepository, productOrder));
    }

    public List<ProductOrder> getOrdersByCustomerId(Long customerId) {
        return productOrderRepository.findByCustomerId(customerId);
    }

    public List<ProductOrder> getOrdersByProductId(Long productId) {
        return productOrderRepository.findByProductId(productId);
    }
}
