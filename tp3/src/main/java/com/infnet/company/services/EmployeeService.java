package com.infnet.company.services;

import com.infnet.company.models.Customer;
import com.infnet.company.models.Employee;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.repositories.EmployeeRepository;
import com.infnet.company.requests.Customer.CreateCustomerRequest;
import com.infnet.company.requests.Customer.UpdateCustomerRequest;
import com.infnet.company.requests.Employee.CreateEmployeeRequest;
import com.infnet.company.requests.Employee.UpdateEmployeeRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário inexistente"
                ));
    }

    public void deleteEmployee(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Funcionário inexistente"
                ));

        employeeRepository.delete(employee);
    }

    public Employee createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        return employeeRepository.save(request.CreateEmployee());
    }

    public void updateEmployee(UpdateEmployeeRequest request) {
        var employee = employeeRepository.findById(request.id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário inexistente"));

        if (employeeRepository.existsByEmailAndIdNot(request.email(), request.id()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        employeeRepository.save(request.UpdateEmployee(employee));
    }
}
