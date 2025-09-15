package com.infnet.company.requests.Customer;

import com.infnet.company.models.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record UpdateCustomerRequest(

        @Positive(message = "O ID é obrigratório")
        Long id,

        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,

        @NotBlank(message = "O primeiro nome é obrigatório")
        @Length(max = 100, message = "O primeiro nome deve ter no máximo 100 caracteres.")
        String firstName,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Length(max = 100, message = "O primeiro nome deve ter no máximo 100 caracteres.")
        String lastName,

        @Email(message = "Email inválido")
        @NotBlank(message = "O email é obrigatório")
        @Length(max = 255, message = "O e-mail deve ter no máximo 255 caracteres.")
        String email,

        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
        String phoneNumber

) {
        public Customer UpdateCustomer(Customer customer) {
                customer.setCpf(cpf);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);
                customer.setPhoneNumber(phoneNumber);
                return customer;
        }
}