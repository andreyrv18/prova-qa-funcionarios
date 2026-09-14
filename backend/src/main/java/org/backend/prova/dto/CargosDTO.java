package org.backend.prova.dto;

import org.backend.prova.model.CargosModel;

import java.io.Serializable;

public class CargosDTO implements Serializable {
  private String codigoDoCargo;
  private String descricaoDoCargo;

  public CargosDTO() {}

  public CargosDTO(CargosModel cargos) {
    this.codigoDoCargo = cargos.getCodigoDoCargo();
    this.descricaoDoCargo = cargos.getDescricaoDoCargo();
  }

  public String getCodigoDoCargo() {
    return codigoDoCargo;
  }

  public void setCodigoDoCargo(String codigoDoCargo) {
    this.codigoDoCargo = codigoDoCargo;
  }

  public String getDescricaoDoCargo() {
    return descricaoDoCargo;
  }

  public void setDescricaoDoCargo(String descricaoDoCargo) {
    this.descricaoDoCargo = descricaoDoCargo;
  }
}
