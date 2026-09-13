package org.backend.prova.service;

import java.util.List;
import org.backend.prova.model.VinculosModel;
import org.backend.prova.repository.VinculosRepository;
import org.springframework.stereotype.Service;

@Service
public class VinculosService {
  private final VinculosRepository vinculosRepository;

  public VinculosService(VinculosRepository vinculosRepository) {
    this.vinculosRepository = vinculosRepository;
  }

  public List<VinculosModel> findAllVinculos() {
    return vinculosRepository.findAll();
  }
}
