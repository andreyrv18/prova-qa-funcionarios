package org.backend.prova.controller;

import org.backend.prova.service.DepartamentosService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departamentos")
public class DepartamentosController {

  private final DepartamentosService departamentosService;

  public DepartamentosController(DepartamentosService departamentosService) {
    this.departamentosService = departamentosService;
  }
}
