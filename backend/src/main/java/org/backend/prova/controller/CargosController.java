package org.backend.prova.controller;

import org.backend.prova.dto.CargosDTO;
import org.backend.prova.model.CargosModel;
import org.backend.prova.service.CargosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargosController {

  private final CargosService cargosService;

  public CargosController(CargosService cargosService) {
    this.cargosService = cargosService;
  }

  @GetMapping("/list")
  public ResponseEntity<List<CargosDTO>> getAllCargos() throws Exception {
    List<CargosDTO> listaTodosCargos = cargosService.listaTodosCargos();

    return ResponseEntity.ok().body(listaTodosCargos);
  }
}
