package org.backend.prova.controller;

import org.backend.prova.dto.CargosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.model.CargosModel;
import org.backend.prova.service.CargosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/criar")
  public ResponseEntity<CargosDTO> criarCargo(@RequestBody CargosDTO cargo)
      throws RegraDeNegocioException {
    CargosDTO cargoSalvo = cargosService.salvar(cargo);

    return ResponseEntity.status(HttpStatus.CREATED).body(cargoSalvo);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CargosDTO> buscarCargoPorCodigo(@PathVariable String codigo)
      throws RegraDeNegocioException {
    CargosDTO buscarCargoPorCodigo = cargosService.buscarCargoPorCodigo(codigo);

    return ResponseEntity.ok().body(buscarCargoPorCodigo);
  }

  @GetMapping("/paginado")
  public ResponseEntity<Page<CargosDTO>> listarPaginado(Pageable pageable)
      throws RecursoNaoEncontradoException {
    Page<CargosDTO> listaPaginado = cargosService.listarPaginado(pageable);
    return ResponseEntity.ok().body(listaPaginado);
  }

  @GetMapping("/pesquisar")
  public ResponseEntity<?> pesquisaCargo(@RequestParam(required = false) String descricaoDoCargo) {

    List<CargosDTO> resultado = cargosService.pesquisarPorDescricao(descricaoDoCargo);
    return ResponseEntity.ok(resultado);
  }
}
