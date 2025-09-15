package com.infnet.company.services;

import com.infnet.company.models.Supplier;
import com.infnet.company.repositories.SupplierRepository;
import com.infnet.company.requests.Supplier.CreateSupplierRequest;
import com.infnet.company.requests.Supplier.UpdateSupplierRequest;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    public List<Supplier> getAllSuppliers() {
        var suppliers = supplierRepository.findAll();
        for(var supplier : suppliers) {
            Hibernate.initialize(supplier.getProducts());
        }
        return suppliers;
    }

    public Supplier getSupplierById(Long id) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor inexistente"));
        Hibernate.initialize(supplier.getProducts());
        return supplier;
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    public Supplier createSupplier(CreateSupplierRequest request) {
        if (supplierRepository.existsByCnpj(request.cnpj()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado");
        if (supplierRepository.existsByEmail(request.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        return supplierRepository.save(request.createSupplier());
    }

    public void updateSupplier(UpdateSupplierRequest request) {
        var supplier = supplierRepository.findById(request.id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor inexistente"));
        if (supplierRepository.existsByCnpjAndIdNot(request.cnpj(), request.id()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado");
        if (supplierRepository.existsByEmailAndIdNot(request.email(), request.id()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        supplierRepository.save(request.updateSupplier(supplier));
    }
}