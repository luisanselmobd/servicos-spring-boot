package com.infnet.company.controllers;

import com.infnet.company.models.ProductOrder;
import com.infnet.company.requests.ProductOrder.CreateProductOrderRequest;
import com.infnet.company.requests.ProductOrder.UpdateProductOrderRequest;
import com.infnet.company.responses.ResponseBase;
import com.infnet.company.services.ProductOrderService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class ProductOrderController {

    private final ProductOrderService orderService;

    public ProductOrderController(ProductOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Pedido cadastrado")
    public ResponseEntity<ResponseBase<Long>> createOrder(@Valid @RequestBody CreateProductOrderRequest request) {
        ProductOrder saved = orderService.createOrder(request);
        return ResponseEntity.status(201).body(new ResponseBase<>(saved.getId(), "Pedido criado"));
    }

    @PutMapping
    @ApiResponse(responseCode = "204", description = "Pedido atualizado")
    public ResponseEntity<ResponseBase<ProductOrder>> updateOrder(@Valid @RequestBody UpdateProductOrderRequest request) {
        orderService.updateOrder(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ResponseBase<List<ProductOrder>>> getAllOrders() {
        return ResponseEntity.ok(new ResponseBase<>(orderService.getAllOrders(), "Lista de pedidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<ProductOrder>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseBase<>(orderService.getOrderById(id), "Pedido encontrado"));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Pedido excluído")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
