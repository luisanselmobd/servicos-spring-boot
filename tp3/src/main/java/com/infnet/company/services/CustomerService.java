package com.infnet.company.services;

import com.infnet.company.models.Customer;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.requests.Customer.CreateCustomerRequest;
import com.infnet.company.requests.Customer.UpdateCustomerRequest;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ProductOrderService productOrderService;
    public CustomerService(CustomerRepository customerRepository, ProductOrderService productOrderService) {
        this.customerRepository = customerRepository;
        this.productOrderService = productOrderService;
    }
    @Transactional
    public List<Customer> getAllCustomers() {
        var customers = customerRepository.findAll();
        for(var customer : customers) {
            Hibernate.initialize(customer.getProductOrders());
        }
        return customers;
    }
    @Transactional
    public Customer getCustomerById(long id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário inexistente"
                ));
        Hibernate.initialize(customer.getProductOrders());

        return customer;
    }

    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário inexistente"
                ));

        customerRepository.delete(customer);
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByCpf(request.cpf()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        if (customerRepository.existsByEmail(request.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        return customerRepository.save(request.CreateCustomer());
    }

    public void updateCustomer(UpdateCustomerRequest request) {
        var customer = customerRepository.findById(request.id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário inexistente"));
        if (customerRepository.existsByCpfAndIdNot(request.cpf(), request.id()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        if (customerRepository.existsByEmailAndIdNot(request.email(), request.id()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");

        customerRepository.save(request.UpdateCustomer(customer));
    }
}
