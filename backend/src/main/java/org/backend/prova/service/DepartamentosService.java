package org.backend.prova.service;

import java.util.List;
import org.backend.prova.model.DepartamentosModel;
import org.backend.prova.repository.DepartamentosRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartamentosService {
  private final DepartamentosRepository departamentosRepository;

  public DepartamentosService(DepartamentosRepository departamentosRepository) {
    this.departamentosRepository = departamentosRepository;
  }

  public List<DepartamentosModel> findAllDepartamentos() {
    return departamentosRepository.findAll();
  }
}
