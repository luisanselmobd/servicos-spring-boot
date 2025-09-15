package com.infnet.company.controllers;

import com.infnet.company.models.Supplier;
import com.infnet.company.requests.Supplier.CreateSupplierRequest;
import com.infnet.company.requests.Supplier.UpdateSupplierRequest;
import com.infnet.company.responses.ResponseBase;
import com.infnet.company.services.SupplierService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseBase<List<Supplier>>> getAllSuppliers() {
        return ResponseEntity.ok(new ResponseBase<>(supplierService.getAllSuppliers(), "Lista de fornecedores"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<Supplier>> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseBase<>(supplierService.getSupplierById(id), "Fornecedor encontrado"));
    }

    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "Fornecedor criado")
    public ResponseEntity<ResponseBase<Long>> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        Supplier supplier = supplierService.createSupplier(request);
        return ResponseEntity.status(201).body(new ResponseBase<>(supplier.getId(), "Fornecedor criado"));
    }

    @PutMapping("")
    @ApiResponse(responseCode = "204", description = "Fornecedor atualizado")
    public ResponseEntity<Void> updateSupplier(@Valid @RequestBody UpdateSupplierRequest request) {
        supplierService.updateSupplier(request);
        return ResponseEntity.noContent().build();    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Fornecedor deletado")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();     }
}