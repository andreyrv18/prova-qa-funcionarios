package org.backend.prova.service;

import java.util.List;
import org.backend.prova.model.FuncionariosModel;
import org.backend.prova.repository.FuncionariosRepository;
import org.springframework.stereotype.Service;

@Service
public class FuncionariosService {
  private final FuncionariosRepository funcionariosRepository;

  public FuncionariosService(FuncionariosRepository funcionariosRepository) {
    this.funcionariosRepository = funcionariosRepository;
  }

  public List<FuncionariosModel> findAllFuncionarios() {
    return funcionariosRepository.findAll();
  }
}
