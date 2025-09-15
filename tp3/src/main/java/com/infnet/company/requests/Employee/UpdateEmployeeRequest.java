package com.infnet.company.requests.Employee;

import com.infnet.company.models.Employee;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record UpdateEmployeeRequest(

        @Positive(message = "O ID é obrigatório")
        Long id,

        @NotBlank(message = "O primeiro nome é obrigatório")
        @Length(max = 100, message = "O primeiro nome deve ter no máximo 100 caracteres.")
        String firstName,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Length(max = 100, message = "O sobrenome deve ter no máximo 100 caracteres.")
        String lastName,

        @Email(message = "Email inválido")
        @NotBlank(message = "O email é obrigatório")
        @Length(max = 255, message = "O e-mail deve ter no máximo 255 caracteres.")
        String email,

        @Pattern(regexp = "\\d{10,15}", message = "Telefone deve ter entre 10 e 15 dígitos")
        String phoneNumber,

        @NotNull(message = "O salário é obrigatório")
        @Positive(message = "O salário deve ser positivo")
        BigDecimal salary,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean active
) {
        public Employee UpdateEmployee(Employee employee) {
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setEmail(email);
                employee.setPhoneNumber(phoneNumber);
                employee.setSalary(salary);
                employee.setActive(active);
                return employee;
        }
}
