package org.backend.prova.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;

@Entity
@Table(name = "departamentos")
@SQLDelete(sql = "UPDATE departamentos SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class DepartamentosModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private @Nullable Long id;

  @Column(name = "codigo_do_departamento", nullable = false, unique = true)
  private String codigoDoDepartamento;

  @Column(name = "descricao_do_departamento", nullable = false, unique = true)
  private String descricaoDoDepartamento;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public DepartamentosModel() {}

  DepartamentosModel(
      @Nullable Long id, String codigoDoDepartamento, String descricaoDoDepartamento) {
    this.id = id;
    this.codigoDoDepartamento = codigoDoDepartamento;
    this.descricaoDoDepartamento = descricaoDoDepartamento;
  }

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable Long id) {
    this.id = id;
  }

  public String getDescricaoDoDepartamento() {
    return descricaoDoDepartamento;
  }

  public void setDescricaoDoDepartamento(String descricaoDoDepartamento) {
    this.descricaoDoDepartamento = descricaoDoDepartamento;
  }

  public String getCodigoDoDepartamento() {
    return codigoDoDepartamento;
  }

  public void setCodigoDoDepartamento(String codigoDoDepartamento) {
    this.codigoDoDepartamento = codigoDoDepartamento;
  }
}
