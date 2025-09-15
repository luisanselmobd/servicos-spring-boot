package com.infnet.company.services;

import com.infnet.company.models.Employee;
import com.infnet.company.repositories.EmployeeRepository;
import com.infnet.company.requests.Employee.CreateEmployeeRequest;
import com.infnet.company.requests.Employee.UpdateEmployeeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("João");
        employee.setEmail("joao@test.com");
    }

    @Test
    void getAllEmployees_HappyPath() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        var result = employeeService.getAllEmployees();

        assert(result.size() == 1);
        verify(employeeRepository).findAll();
    }

    @Test
    void getEmployeeById_HappyPath() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        var result = employeeService.getEmployeeById(1L);

        assert(result.getId() == 1L);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Funcionário inexistente")
                .extracting("statusCode").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createEmployee_HappyPath() {
        CreateEmployeeRequest request = mock(CreateEmployeeRequest.class);
        when(request.email()).thenReturn("joao@test.com");
        when(request.CreateEmployee()).thenReturn(employee);

        when(employeeRepository.existsByEmail("joao@test.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        var result = employeeService.createEmployee(request);

        assert(result.getEmail().equals("joao@test.com"));
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_DuplicatedEmail() {
        CreateEmployeeRequest request = mock(CreateEmployeeRequest.class);
        when(request.email()).thenReturn("joao@test.com");

        when(employeeRepository.existsByEmail("joao@test.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("E-mail já cadastrado")
                .extracting("statusCode").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateEmployee_HappyPath() {
        UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
        when(request.id()).thenReturn(1L);
        when(request.email()).thenReturn("joao@test.com");
        when(request.UpdateEmployee(employee)).thenReturn(employee);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("joao@test.com")).thenReturn(false);

        employeeService.updateEmployee(request);

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_NotFound() {
        UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
        when(request.id()).thenReturn(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Funcionário inexistente")
                .extracting("statusCode").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateEmployee_DuplicatedEmail() {
        UpdateEmployeeRequest request = mock(UpdateEmployeeRequest.class);
        when(request.id()).thenReturn(1L);
        when(request.email()).thenReturn("joao@test.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("joao@test.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.updateEmployee(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("E-mail já cadastrado")
                .extracting("statusCode").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void deleteEmployee_HappyPath() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Funcionário inexistente")
                .extracting("statusCode").isEqualTo(HttpStatus.NOT_FOUND);
    }
}
