package com.infnet.company.services;

import com.infnet.company.models.Customer;
import com.infnet.company.models.Product;
import com.infnet.company.models.ProductOrder;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.repositories.ProductOrderRepository;
import com.infnet.company.repositories.ProductRepository;
import com.infnet.company.requests.ProductOrder.CreateProductOrderRequest;
import com.infnet.company.requests.ProductOrder.UpdateProductOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductOrderServiceTest {

    @Mock private ProductOrderRepository productOrderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private ProductOrderService service;

    private ProductOrder order;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        product = new Product();
        product.setId(2L);
        order = new ProductOrder();
        order.setId(10L);
        order.setCustomer(customer);
        order.setProduct(product);
    }

    @Test
    void getAllOrders_HappyPath() {
        when(productOrderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<ProductOrder> result = service.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        verify(productOrderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_HappyPath() {
        when(productOrderRepository.findById(10L)).thenReturn(Optional.of(order));

        ProductOrder result = service.getOrderById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void getOrderById_NotFound() {
        when(productOrderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.getOrderById(99L));
    }

    @Test
    void deleteOrder_HappyPath() {
        when(productOrderRepository.findById(10L)).thenReturn(Optional.of(order));

        service.deleteOrder(10L);

        verify(productOrderRepository, times(1)).delete(order);
    }

    @Test
    void deleteOrder_NotFound() {
        when(productOrderRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.deleteOrder(10L));
    }

    @Test
    void createOrder_HappyPath() {
        CreateProductOrderRequest request = mock(CreateProductOrderRequest.class);
        when(request.customerId()).thenReturn(1L);
        when(request.productId()).thenReturn(2L);
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(productRepository.existsById(2L)).thenReturn(true);
        when(request.createProductOrder(productRepository, customerRepository)).thenReturn(order);
        when(productOrderRepository.save(order)).thenReturn(order);

        ProductOrder result = service.createOrder(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void createOrder_CustomerNotFound() {
        CreateProductOrderRequest request = mock(CreateProductOrderRequest.class);
        when(request.customerId()).thenReturn(1L);
        when(customerRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> service.createOrder(request));
    }

    @Test
    void createOrder_ProductNotFound() {
        CreateProductOrderRequest request = mock(CreateProductOrderRequest.class);
        when(request.customerId()).thenReturn(1L);
        when(request.productId()).thenReturn(2L);
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(productRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> service.createOrder(request));
    }

    @Test
    void updateOrder_OrderNotFound() {
        UpdateProductOrderRequest request = mock(UpdateProductOrderRequest.class);
        when(request.id()).thenReturn(10L);
        when(productOrderRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.updateOrder(request));
    }

    @Test
    void updateOrder_CustomerNotFound() {
        UpdateProductOrderRequest request = mock(UpdateProductOrderRequest.class);
        when(request.id()).thenReturn(10L);
        when(productOrderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(customerRepository.existsById(1L)).thenReturn(false);
        when(request.customerId()).thenReturn(1L);

        assertThrows(ResponseStatusException.class,
                () -> service.updateOrder(request));
    }

    @Test
    void updateOrder_ProductNotFound() {
        UpdateProductOrderRequest request = mock(UpdateProductOrderRequest.class);
        when(request.id()).thenReturn(10L);
        when(productOrderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(request.customerId()).thenReturn(1L);
        when(request.productId()).thenReturn(2L);
        when(productRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> service.updateOrder(request));
    }

    @Test
    void getOrdersByCustomerId_HappyPath() {
        when(productOrderRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(order));

        List<ProductOrder> result = service.getOrdersByCustomerId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getOrdersByProductId_HappyPath() {
        when(productOrderRepository.findByProductId(2L)).thenReturn(Arrays.asList(order));

        List<ProductOrder> result = service.getOrdersByProductId(2L);

        assertEquals(1, result.size());
    }
}
