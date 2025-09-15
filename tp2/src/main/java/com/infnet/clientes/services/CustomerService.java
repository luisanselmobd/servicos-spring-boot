package com.infnet.clientes.services;

import com.infnet.clientes.models.Customer;
import com.infnet.clientes.requests.Customers.CreateCustomerRequest;
import com.infnet.clientes.requests.Customers.UpdateCustomerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class CustomerService {

    private final Map<Integer, Customer> customers = new HashMap<>();
    private int nextId;

    public CustomerService() {
        customers.put(1, new Customer(1, "12345678910", "João", "Silva", "joao.silva@email.com", "11987654321"));
        customers.put(2, new Customer(2, "12345678911", "Maria", "Santos", "maria.santos@email.com", "21876543210"));
        customers.put(3, new Customer(3, "12345678912", "Pedro", "Oliveira", "pedro.oliveira@email.com", "31765432109"));
        customers.put(4, new Customer(4, "12345678913", "Ana", "Costa", "ana.costa@email.com", "41654321098"));
        customers.put(5, new Customer(5, "12345678914", "Carlos", "Pereira", "carlos.pereira@email.com", "51543210987"));
        nextId = 6;
    }

    public ArrayList<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer getCustomerById(int id) {
        if (customerNotExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário inexistente");
        return customers.get(id);
    }

    public void deleteCustomer(int id) {
        if (customerNotExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário inexistente");
        customers.remove(id);
    }

    public Customer createCustomer(CreateCustomerRequest request) {

        if (!isCpfAvailable(request.cpf())) throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        if (!isEmailAvailable(request.email())) throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");

        Customer customer = request.CreateCustomer(nextId);
        customers.put(nextId, customer);
        nextId++;

        return customer;
    }

    public void updateCustomer(UpdateCustomerRequest request) {

        if (customerNotExists(request.id())) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário inexistente");
        if (!isCpfAvailable(request.cpf(), request.id())) throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        if (!isEmailAvailable(request.email(), request.id())) throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");

        Customer updatedCustomer = request.CreateCustomer();
        customers.replace(request.id(), updatedCustomer);
    }

    private boolean customerNotExists(int id) {
        return !customers.containsKey(id);
    }

    private boolean isCpfAvailable(String cpf) {
        return customers.values()
                .stream()
                .noneMatch(c -> Objects.equals(c.getCpf(), cpf));
    }

    private boolean isEmailAvailable(String email) {
        return customers.values()
                .stream()
                .noneMatch(c -> Objects.equals(c.getEmail(), email));
    }

    private boolean isCpfAvailable(String cpf, int excludeId) {
        return customers.values()
                .stream()
                .noneMatch(c -> !Objects.equals(c.getId(), excludeId) && Objects.equals(c.getCpf(), cpf));
    }

    private boolean isEmailAvailable(String email, int excludeId) {
        return customers.values()
                .stream()
                .noneMatch(c -> !Objects.equals(c.getId(), excludeId) && Objects.equals(c.getEmail(), email));
    }
}
