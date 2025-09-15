package com.infnet.company.services;

import com.infnet.company.models.Product;
import com.infnet.company.repositories.ProductRepository;
import com.infnet.company.repositories.SupplierRepository;
import com.infnet.company.requests.Product.CreateProductRequest;
import com.infnet.company.requests.Product.UpdateProductRequest;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository,
                          SupplierRepository supplierRepository,
                          SupplierService supplierService) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.supplierService = supplierService;
    }

    @Transactional
    public List<Product> getAllProducts() {
        var products = productRepository.findAll();
        for (Product product : products) {
            Hibernate.initialize(product.getSupplier());
        }
        return products;
    }
    @Transactional
    public Product getProductById(Long id) {
        var product =  productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto inexistente"));
        Hibernate.initialize(product.getSupplier());
        return product;
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public Product createProduct(CreateProductRequest request) {
        if (productRepository.existsByName(request.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto já cadastrado");
        return productRepository.save(request.createProduct(supplierRepository));
    }

    public void updateProduct(UpdateProductRequest request) {
        var product = productRepository.findById(request.id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto inexistente"));
        if (productRepository.existsByNameAndIdNot(request.name(), request.id()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto já cadastrado");
        productRepository.save(request.updateProduct(supplierRepository, product));
    }
}