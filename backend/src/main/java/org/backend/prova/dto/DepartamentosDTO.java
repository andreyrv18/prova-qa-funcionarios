package org.backend.prova.dto;

import org.backend.prova.model.DepartamentosModel;

import java.io.Serializable;

public class DepartamentosDTO implements Serializable {
    private String codigoDoDepartamento;
    private String descricaoDoDepartamento;

    public DepartamentosDTO() {}

    public DepartamentosDTO(DepartamentosModel Departamentos) {
        this.codigoDoDepartamento = Departamentos.getCodigoDoDepartamento();
        this.descricaoDoDepartamento = Departamentos.getDescricaoDoDepartamento();
    }

    public String getCodigoDoDepartamento() {
        return codigoDoDepartamento;
    }

    public void setCodigoDoDepartamento(String codigoDoDepartamento) {
        this.codigoDoDepartamento = codigoDoDepartamento;
    }

    public String getDescricaoDoDepartamento() {
        return descricaoDoDepartamento;
    }

    public void setDescricaoDoDepartamento(String descricaoDoDepartamento) {
        this.descricaoDoDepartamento = descricaoDoDepartamento;
    }
}
