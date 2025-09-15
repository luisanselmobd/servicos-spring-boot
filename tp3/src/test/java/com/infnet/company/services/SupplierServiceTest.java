package com.infnet.company.services;

import com.infnet.company.models.Supplier;
import com.infnet.company.repositories.SupplierRepository;
import com.infnet.company.requests.Supplier.CreateSupplierRequest;
import com.infnet.company.requests.Supplier.UpdateSupplierRequest;
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

class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Fornecedor X");
        supplier.setCnpj("12345678901234");
        supplier.setEmail("fornecedor@test.com");
    }

    @Test
    void getAllSuppliers_HappyPath() {
        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier));

        List<Supplier> result = supplierService.getAllSuppliers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fornecedor X", result.get(0).getName());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void getSupplierById_HappyPath() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Supplier result = supplierService.getSupplierById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fornecedor X", result.getName());
    }

    @Test
    void getSupplierById_NotFound() {
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> supplierService.getSupplierById(99L));

        assertEquals("404 NOT_FOUND \"Fornecedor inexistente\"", ex.getMessage());
    }

    @Test
    void deleteSupplier_HappyPath() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(1L);

        verify(supplierRepository).delete(supplier);
    }

    @Test
    void deleteSupplier_NotFound() {
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> supplierService.deleteSupplier(99L));

        assertEquals("404 NOT_FOUND \"Fornecedor inexistente\"", ex.getMessage());
    }

    @Test
    void createSupplier_HappyPath() {
        CreateSupplierRequest request = mock(CreateSupplierRequest.class);
        when(request.cnpj()).thenReturn("12345678901234");
        when(request.email()).thenReturn("fornecedor@test.com");
        when(supplierRepository.existsByCnpj("12345678901234")).thenReturn(false);
        when(supplierRepository.existsByEmail("fornecedor@test.com")).thenReturn(false);
        when(request.createSupplier()).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        Supplier result = supplierService.createSupplier(request);

        assertNotNull(result);
        assertEquals("Fornecedor X", result.getName());
        verify(supplierRepository).save(supplier);
    }

    @Test
    void createSupplier_CnpjConflict() {
        CreateSupplierRequest request = mock(CreateSupplierRequest.class);
        when(request.cnpj()).thenReturn("12345678901234");
        when(supplierRepository.existsByCnpj("12345678901234")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> supplierService.createSupplier(request));

        assertEquals("409 CONFLICT \"CNPJ já cadastrado\"", ex.getMessage());
    }

    @Test
    void createSupplier_EmailConflict() {
        CreateSupplierRequest request = mock(CreateSupplierRequest.class);
        when(request.cnpj()).thenReturn("12345678901234");
        when(request.email()).thenReturn("fornecedor@test.com");
        when(supplierRepository.existsByCnpj("12345678901234")).thenReturn(false);
        when(supplierRepository.existsByEmail("fornecedor@test.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> supplierService.createSupplier(request));

        assertEquals("409 CONFLICT \"Email já cadastrado\"", ex.getMessage());
    }

    @Test
    void updateSupplier_HappyPath() {
        UpdateSupplierRequest request = mock(UpdateSupplierRequest.class);
        when(request.id()).thenReturn(1L);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(request.updateSupplier(supplier)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        assertDoesNotThrow(() -> supplierService.updateSupplier(request));

        verify(supplierRepository).save(supplier);
    }

    @Test
    void updateSupplier_NotFound() {
        UpdateSupplierRequest request = mock(UpdateSupplierRequest.class);
        when(request.id()).thenReturn(99L);
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> supplierService.updateSupplier(request));

        assertEquals("404 NOT_FOUND \"Fornecedor inexistente\"", ex.getMessage());
    }
}
