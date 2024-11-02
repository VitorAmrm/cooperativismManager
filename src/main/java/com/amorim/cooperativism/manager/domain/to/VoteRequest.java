package com.amorim.cooperativism.manager.domain.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public class VoteRequest {
    @NotNull(message = "O voto tem que ter algum valor")
    private Boolean value;
    @NotNull(message = "Cpf não pode ser nulo")
    @Size(max = 11, min = 11, message = "Valor invalido")
    @CPF(message = "Formato do cpf invalido")
    private String nationalId;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId( String nationalId) {
        this.nationalId = nationalId;
    }
}
