package org.backend.prova.controller;

import org.backend.prova.service.VinculosService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vinculos")
public class VinculosController {

  private final VinculosService vinculosService;

  public VinculosController(VinculosService vinculosService) {
    this.vinculosService = vinculosService;
  }
}
