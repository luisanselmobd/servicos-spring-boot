package com.infnet.company.controllers;

import com.infnet.company.models.Customer;
import com.infnet.company.requests.Customer.CreateCustomerRequest;
import com.infnet.company.requests.Customer.UpdateCustomerRequest;
import com.infnet.company.responses.ResponseBase;
import com.infnet.company.services.CustomerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseBase<List<Customer>>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(new ResponseBase<>(customers, "Lista de clientes cadastrados"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<Customer>> getCustomerById(@PathVariable long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(new ResponseBase<>(customer, "Cliente encontrado"));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Cliente deletado")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado")
    public ResponseEntity<ResponseBase<Long>> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.createCustomer(request);
        return ResponseEntity.status(201).body(new ResponseBase<>(customer.getId(), "Cliente cadastrado"));
    }

    @PutMapping("")
    @ApiResponse(responseCode = "204", description = "Cliente atualizado")
    public ResponseEntity<Void> updateCustomer(@Valid @RequestBody UpdateCustomerRequest request) {
        customerService.updateCustomer(request);
        return ResponseEntity.noContent().build();
    }
}
