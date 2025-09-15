package com.infnet.company.controllers;

import com.infnet.company.models.Product;
import com.infnet.company.requests.Product.CreateProductRequest;
import com.infnet.company.requests.Product.UpdateProductRequest;
import com.infnet.company.responses.ResponseBase;
import com.infnet.company.services.ProductService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Produto cadastrado")
    public ResponseEntity<ResponseBase<Long>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.status(201).body(new ResponseBase<>(product.getId(), "Produto criado"));
    }

    @PutMapping
    @ApiResponse(responseCode = "204", description = "Produto atualizado")
    public ResponseEntity<Void> updateProduct(@Valid @RequestBody UpdateProductRequest request) {
        productService.updateProduct(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseBase<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ResponseBase<>(products, "Lista de produtos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(new ResponseBase<>(product, "Produto encontrado"));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Produto deletado")
    public ResponseEntity<ResponseBase<Object>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}