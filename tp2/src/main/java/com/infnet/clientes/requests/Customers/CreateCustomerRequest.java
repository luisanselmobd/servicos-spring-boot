package com.infnet.clientes.requests.Customers;
import com.infnet.clientes.models.Customer;
import jakarta.validation.constraints.*;
public record CreateCustomerRequest(

        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @NotBlank(message = "O primeiro nome é obrigatório")
        String firstName,

        @NotBlank(message = "O sobrenome é obrigatório")
        String lastName,

        @Email(message = "Email inválido")
        @NotBlank(message = "O email é obrigatório")
        String email,

        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
        String phoneNumber

) {
        public Customer CreateCustomer(int id) {
                return new Customer(id, cpf, firstName, lastName, email, phoneNumber);
        }



}