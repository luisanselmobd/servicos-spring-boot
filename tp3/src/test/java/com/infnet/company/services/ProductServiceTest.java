package com.infnet.company.services;

import com.infnet.company.models.Product;
import com.infnet.company.models.Supplier;
import com.infnet.company.repositories.ProductRepository;
import com.infnet.company.repositories.SupplierRepository;
import com.infnet.company.requests.Product.CreateProductRequest;
import com.infnet.company.requests.Product.UpdateProductRequest;
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

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        var supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Fornecedor X");
        product.setSupplier(supplier);
    }

    @Test
    void getAllProducts_HappyPath() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Produto Teste", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_HappyPath() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Produto Teste", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> productService.getProductById(99L));

        assertEquals("404 NOT_FOUND \"Produto inexistente\"", ex.getMessage());
    }

    @Test
    void deleteProduct_HappyPath() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void createProduct_HappyPath() {
        CreateProductRequest request = mock(CreateProductRequest.class);
        when(request.name()).thenReturn("Novo Produto");
        when(productRepository.existsByName("Novo Produto")).thenReturn(false);
        when(request.createProduct(supplierRepository)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("Produto Teste", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    void createProduct_Conflict() {
        CreateProductRequest request = mock(CreateProductRequest.class);
        when(request.name()).thenReturn("Produto Teste");
        when(productRepository.existsByName("Produto Teste")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> productService.createProduct(request));

        assertEquals("409 CONFLICT \"Produto já cadastrado\"", ex.getMessage());
    }

    @Test
    void updateProduct_HappyPath() {
        UpdateProductRequest request = mock(UpdateProductRequest.class);
        when(request.id()).thenReturn(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(request.updateProduct(supplierRepository, product)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        assertDoesNotThrow(() -> productService.updateProduct(request));

        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_NotFound() {
        UpdateProductRequest request = mock(UpdateProductRequest.class);
        when(request.id()).thenReturn(99L);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> productService.updateProduct(request));

        assertEquals("404 NOT_FOUND \"Produto inexistente\"", ex.getMessage());
    }
}
