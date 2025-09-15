package com.infnet.company.services;

import com.infnet.company.models.Customer;
import com.infnet.company.repositories.CustomerRepository;
import com.infnet.company.requests.Customer.CreateCustomerRequest;
import com.infnet.company.requests.Customer.UpdateCustomerRequest;
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

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductOrderService productOrderService;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setCpf("12345678901");
        customer.setEmail("test@test.com");
    }

    @Test
    void getAllCustomers_HappyPath() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        var result = customerService.getAllCustomers();

        verify(customerRepository, times(1)).findAll();
        assert(result.size() == 1);
    }

    @Test
    void getCustomerById_HappyPath() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        var result = customerService.getCustomerById(1L);

        assert(result.getId() == 1L);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuário inexistente")
                .extracting("statusCode").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createCustomer_HappyPath() {
        CreateCustomerRequest request = mock(CreateCustomerRequest.class);
        when(request.cpf()).thenReturn("12345678901");
        when(request.email()).thenReturn("test@test.com");
        when(request.CreateCustomer()).thenReturn(customer);

        when(customerRepository.existsByCpf("12345678901")).thenReturn(false);
        when(customerRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        var result = customerService.createCustomer(request);

        verify(customerRepository).save(any(Customer.class));
        assert(result.getCpf().equals("12345678901"));
    }

    @Test
    void createCustomer_DuplicatedCpf() {
        CreateCustomerRequest request = mock(CreateCustomerRequest.class);
        when(request.cpf()).thenReturn("12345678901");

        when(customerRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("CPF já cadastrado")
                .extracting("statusCode").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void createCustomer_DuplicatedEmail() {
        CreateCustomerRequest request = mock(CreateCustomerRequest.class);
        when(request.cpf()).thenReturn("12345678901");
        when(request.email()).thenReturn("test@test.com");

        when(customerRepository.existsByCpf("12345678901")).thenReturn(false);
        when(customerRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("E-mail já cadastrado")
                .extracting("statusCode").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateCustomer_HappyPath() {
        UpdateCustomerRequest request = mock(UpdateCustomerRequest.class);
        when(request.id()).thenReturn(1L);
        when(request.cpf()).thenReturn("12345678901");
        when(request.email()).thenReturn("test@test.com");
        when(request.UpdateCustomer(customer)).thenReturn(customer);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByCpf("12345678901")).thenReturn(false);
        when(customerRepository.existsByEmail("test@test.com")).thenReturn(false);

        customerService.updateCustomer(request);

        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_NotFound() {
        UpdateCustomerRequest request = mock(UpdateCustomerRequest.class);
        when(request.id()).thenReturn(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuário inexistente")
                .extracting("statusCode").isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateCustomer_DuplicatedCpf() {
        UpdateCustomerRequest request = mock(UpdateCustomerRequest.class);
        when(request.id()).thenReturn(1L);
        when(request.cpf()).thenReturn("12345678901");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByCpf("12345678901")).thenReturn(true);

        assertThatThrownBy(() -> customerService.updateCustomer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("CPF já cadastrado")
                .extracting("statusCode").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateCustomer_DuplicatedEmail() {
        UpdateCustomerRequest request = mock(UpdateCustomerRequest.class);
        when(request.id()).thenReturn(1L);
        when(request.cpf()).thenReturn("12345678901");
        when(request.email()).thenReturn("test@test.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByCpf("12345678901")).thenReturn(false);
        when(customerRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThatThrownBy(() -> customerService.updateCustomer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("E-mail já cadastrado")
                .extracting("statusCode").isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void deleteCustomer_HappyPath() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteCustomer(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuário inexistente")
                .extracting("statusCode").isEqualTo(HttpStatus.NOT_FOUND);
    }
}
