package org.backend.prova.dto;

import org.backend.prova.model.VinculosModel;

public class VinculosDTO {
    private Long id;
    private String empresa;
    private String matricula;
    private Long cargoId;
    private String cargoDescricao;
    private Long departamentoId;
    private String departamentoDescricao;

    public VinculosDTO() {}

    public VinculosDTO(Long id, String empresa, String matricula,
                       Long cargoId, String cargoDescricao,
                       Long departamentoId, String departamentoDescricao) {
        this.id = id;
        this.empresa = empresa;
        this.matricula = matricula;
        this.cargoId = cargoId;
        this.cargoDescricao = cargoDescricao;
        this.departamentoId = departamentoId;
        this.departamentoDescricao = departamentoDescricao;
    }

    public VinculosDTO(VinculosModel vinculosModel) {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Long getCargoId() { return cargoId; }
    public void setCargoId(Long cargoId) { this.cargoId = cargoId; }

    public String getCargoDescricao() { return cargoDescricao; }
    public void setCargoDescricao(String cargoDescricao) { this.cargoDescricao = cargoDescricao; }

    public Long getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(Long departamentoId) { this.departamentoId = departamentoId; }

    public String getDepartamentoDescricao() { return departamentoDescricao; }
    public void setDepartamentoDescricao(String departamentoDescricao) {
        this.departamentoDescricao = departamentoDescricao;
    }
}
