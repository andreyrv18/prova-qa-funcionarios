package org.backend.prova.controller;

import org.backend.prova.service.FuncionariosService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funcionarios")
public class FuncionariosController {

  private final FuncionariosService funcionariosService;

  public FuncionariosController(FuncionariosService funcionariosService) {
    this.funcionariosService = funcionariosService;
  }
}
