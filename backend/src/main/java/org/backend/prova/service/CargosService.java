package org.backend.prova.service;

import java.util.List;

import org.backend.prova.dto.CargosDTO;
import org.backend.prova.model.CargosModel;
import org.backend.prova.repository.CargosRepository;
import org.springframework.stereotype.Service;

@Service
public class CargosService {
  private final CargosRepository cargosRepository;

  public CargosService(CargosRepository cargosRepository) {
    this.cargosRepository = cargosRepository;
  }

  public List<CargosDTO> listaTodosCargos() throws Exception {
    List<CargosModel> listaTodosCargos = cargosRepository.findAll();

    if (listaTodosCargos.isEmpty()) {
      throw new Exception("Nenhum cargo encontrado");
    }
    List<CargosDTO> listaDto = listaTodosCargos.stream().map(CargosDTO::new).toList();

    return listaDto;
  }
}
