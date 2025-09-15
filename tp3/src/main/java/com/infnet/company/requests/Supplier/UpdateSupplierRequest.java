package com.infnet.company.requests.Supplier;

import com.infnet.company.models.Supplier;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record UpdateSupplierRequest(

        @Positive(message = "O ID é obrigatório")
        Long id,

        @NotBlank(message = "O nome da empresa é obrigatório")
        @Length(max = 200, message = "O nome da empresa deve ter no máximo 200 caracteres.")
        String name,

        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter 14 dígitos")
        @NotBlank(message = "O CNPJ é obrigatório")
        String cnpj,

        @Email(message = "Email inválido")
        @NotBlank(message = "O email é obrigatório")
        @Length(max = 255, message = "O e-mail deve ter no máximo 255 caracteres.")
        String email
) {
        public Supplier updateSupplier(Supplier supplier) {
                supplier.setName(name);
                supplier.setCnpj(cnpj);
                supplier.setEmail(email);
                return supplier;
        }
}