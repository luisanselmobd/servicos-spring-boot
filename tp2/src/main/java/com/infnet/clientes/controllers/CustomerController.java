package com.infnet.clientes.controllers;
import com.infnet.clientes.models.Customer;
import com.infnet.clientes.requests.Customers.CreateCustomerRequest;
import com.infnet.clientes.requests.Customers.UpdateCustomerRequest;
import com.infnet.clientes.responses.ResponseBase;
import com.infnet.clientes.services.CustomerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        var response = new ResponseBase<>(customers, "Lista de clientes cadastrados");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<Customer>> getCustomerById(@PathVariable int id) {
        Customer customer = customerService.getCustomerById(id);
        var response = new ResponseBase<>(customer, "Cliente encontrado");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Cliente deletado")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado")
    public ResponseEntity<ResponseBase<Integer>> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.createCustomer(request);
        var response = new ResponseBase<>(customer.getId(), "Cliente cadastrado");
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("")
    @ApiResponse(responseCode = "204", description = "Cliente atualizado")
    public ResponseEntity<Void> updateCustomer(
            @Valid @RequestBody UpdateCustomerRequest request) {
        customerService.updateCustomer(request);
        return ResponseEntity.noContent().build();
    }

}
