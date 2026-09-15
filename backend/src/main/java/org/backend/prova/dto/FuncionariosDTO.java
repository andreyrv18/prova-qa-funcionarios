package org.backend.prova.dto;

import org.backend.prova.model.FuncionariosModel;

import java.util.List;
import java.util.stream.Collectors;

public class FuncionariosDTO {
  private Long id;
  private String nome;
  private String cpf;
  private List<VinculosDTO> vinculos;

  public FuncionariosDTO() {}

  public FuncionariosDTO(Long id, String nome, String cpf, List<VinculosDTO> vinculos) {
    this.id = id;
    this.nome = nome;
    this.cpf = cpf;
    this.vinculos = vinculos;
  }

    public FuncionariosDTO(FuncionariosModel model) {
        this.id = model.getId();
        this.nome = model.getNome();
        this.cpf = model.getCpf();
        // Converte os vínculos do model para DTO
        this.vinculos = model.getVinculos().stream()
                .map(VinculosDTO::new) // Assumindo que VinculosDTO tem construtor similar
                .collect(Collectors.toList());
    }

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public List<VinculosDTO> getVinculos() {
    return vinculos;
  }

  public void setVinculos(List<VinculosDTO> vinculos) {
    this.vinculos = vinculos;
  }
}
