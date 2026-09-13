package org.backend.prova.controller;

import org.backend.prova.service.CargosService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cargos")
public class CargosController {

  private final CargosService cargosService;

  public CargosController(CargosService cargosService) {
    this.cargosService = cargosService;
  }
}
