package com.infnet.company.data;

import com.infnet.company.models.*;
import com.infnet.company.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(CustomerRepository customerRepository,
                                   EmployeeRepository employeeRepository,
                                   ProductOrderRepository productOrderRepository,
                                   ProductRepository productRepository,
                                   SupplierRepository supplierRepository) {
        return args -> {
            Supplier supplier1 = new Supplier(null, "Fornecedor A", "12345678000101", "a@supplier.com", null);
            Supplier supplier2 = new Supplier(null, "Fornecedor B", "22345678000101", "b@supplier.com", null);
            Supplier supplier3 = new Supplier(null, "Fornecedor C", "32345678000101", "c@supplier.com", null);

            supplierRepository.saveAll(Arrays.asList(supplier1, supplier2, supplier3));

            Product product1 = new Product(null, "Produto 1", "Descrição produto 1", new BigDecimal("10.50"), true, supplier1, new ArrayList<>());
            Product product2 = new Product(null, "Produto 2", "Descrição produto 2", new BigDecimal("20.00"), true, supplier2, new ArrayList<>());
            Product product3 = new Product(null, "Produto 3", "Descrição produto 3", new BigDecimal("30.00"), true, supplier3, new ArrayList<>());

            productRepository.saveAll(Arrays.asList(product1, product2, product3));

            Customer customer1 = new Customer(null, "11111111111", "João", "Silva", "joao@email.com", "11999999999", null);
            Customer customer2 = new Customer(null, "22222222222", "Maria", "Oliveira", "maria@email.com", "11988888888", null);
            Customer customer3 = new Customer(null, "33333333333", "Pedro", "Santos", "pedro@email.com", "11977777777", null);

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

            Employee employee1 = new Employee(null, "Carlos", "Pereira", "carlos@email.com", "11966666666", new BigDecimal("3000.00"), true);
            Employee employee2 = new Employee(null, "Ana", "Costa", "ana@email.com", "11955555555", new BigDecimal("3500.00"), true);
            Employee employee3 = new Employee(null, "Marcos", "Almeida", "marcos@email.com", "11944444444", new BigDecimal("4000.00"), true);

            employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));

            ProductOrder order1 = new ProductOrder(null, customer1, product1, 2, LocalDateTime.now(), true);
            ProductOrder order2 = new ProductOrder(null, customer2, product2, 1, LocalDateTime.now(), false);
            ProductOrder order3 = new ProductOrder(null, customer3, product3, 5, LocalDateTime.now(), true);

            productOrderRepository.saveAll(Arrays.asList(order1, order2, order3));


            customer1.setProductOrders(Arrays.asList(order1));
            customer2.setProductOrders(Arrays.asList(order2));
            customer3.setProductOrders(Arrays.asList(order3));

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

            supplier1.setProducts(Arrays.asList(product1));
            supplier2.setProducts(Arrays.asList(product2));
            supplier3.setProducts(Arrays.asList(product3));

            supplierRepository.saveAll(Arrays.asList(supplier1, supplier2, supplier3));
        };
    }
}
