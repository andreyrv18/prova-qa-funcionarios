package org.backend.prova.service;

import java.util.List;
import org.backend.prova.model.CargosModel;
import org.backend.prova.repository.CargosRepository;
import org.springframework.stereotype.Service;

@Service
public class CargosService {
  private final CargosRepository cargosRepository;

  public CargosService(CargosRepository cargosRepository) {
    this.cargosRepository = cargosRepository;
  }

  public List<CargosModel> findAllCargos() {
    return cargosRepository.findAll();
  }
}
