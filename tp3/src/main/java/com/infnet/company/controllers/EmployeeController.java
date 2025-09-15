package com.infnet.company.controllers;


import com.infnet.company.models.Employee;
import com.infnet.company.requests.Employee.CreateEmployeeRequest;
import com.infnet.company.requests.Employee.UpdateEmployeeRequest;
import com.infnet.company.responses.ResponseBase;
import com.infnet.company.services.EmployeeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseBase<List<Employee>>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(new ResponseBase<>(employees, "Lista de funcionários cadastrados"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase<Employee>> getEmployeeById(@PathVariable long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ResponseBase<>(employee, "Funcionário encontrado"));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Empregado deletado")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();    }

    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "Empregado cadastrado")
    public ResponseEntity<ResponseBase<Long>> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        Employee employee = employeeService.createEmployee(request);
        return ResponseEntity.status(201).body(new ResponseBase<>(employee.getId(), "Funcionário cadastrado"));
    }

    @PutMapping("")
    @ApiResponse(responseCode = "204", description = "Empregado atualizado")
    public ResponseEntity<Void> updateEmployee(@Valid @RequestBody UpdateEmployeeRequest request) {
        employeeService.updateEmployee(request);
        return ResponseEntity.noContent().build();
    }
}
